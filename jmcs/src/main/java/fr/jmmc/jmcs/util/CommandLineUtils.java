/*******************************************************************************
 *                 jMCS project ( http://www.jmmc.fr/dev/jmcs )
 *******************************************************************************
 * Copyright (c) 2013, CNRS. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the CNRS nor the names of its contributors may be
 *       used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL CNRS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package fr.jmmc.jmcs.util;

import ch.qos.logback.classic.Level;
import fr.jmmc.jmcs.Bootstrapper;
import fr.jmmc.jmcs.data.app.ApplicationDescription;
import fr.jmmc.jmcs.logging.LogbackGui;
import fr.jmmc.jmcs.logging.LoggingService;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import fr.jmmc.jmcs.util.runner.EmptyJobListener;
import fr.jmmc.jmcs.util.runner.JobListener;
import fr.jmmc.jmcs.util.runner.LocalLauncher;
import fr.jmmc.jmcs.util.runner.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper on http://code.google.com/p/vo-urp/ task runner.
 * 
 * @author Sylvain LAFRASSE, Laurent BOURGES.
 */
public final class CommandLineUtils {

    /** Class logger */
    private static final Logger _logger = LoggerFactory.getLogger(CommandLineUtils.class.getName());
    /** Application identifier for LocalLauncher */
    public final static String APP_NAME = "CliStarter";
    /** User for LocalLauncher */
    public final static String USER_NAME = "JMMC";
    /** Task identifier for LocalLauncher */
    public final static String TASK_NAME = "CliStarter";
    /** CLI 'open' token */
    public static final String CLI_OPEN_KEY = "open";

    /**
     * Launch the given command-line path application in background.
     * 
     * @param cliPath command-line path to launch the application (simple one without arguments only)
     * @return the job context identifier
     * @throws IllegalStateException if the job can not be submitted to the job queue
     */
    public static Long exec(final String cliPath) throws IllegalStateException {
        return exec(cliPath, new EmptyJobListener());
    }

    /**
     * Launch the given command-line path application in background.
     * 
     * @param cliPath command-line path to launch the application (simple one without arguments only)
     * @param jobListener job event listener (not null)
     * @return the job context identifier
     * @throws IllegalStateException if the job can not be submitted to the job queue
     */
    public static Long exec(final String cliPath, final JobListener jobListener) throws IllegalStateException {

        if (StringUtils.isEmpty(cliPath)) {
            throw new IllegalArgumentException("empty command-line path !");
        }
        if (jobListener == null) {
            throw new IllegalArgumentException("undefined job listener !");
        }

        _logger.info("launch: {}", cliPath);

        // Create the execution context without log file
        final RootContext jobContext = LocalLauncher.prepareMainJob(APP_NAME, USER_NAME, FileUtils.getTempDirPath(), null);

        // TODO : split command-line path to handle arguments ansd so on...
        final String[] cmd = new String[]{cliPath};
        LocalLauncher.prepareChildJob(jobContext, TASK_NAME, cmd);

        // Puts the job in the job queue (can throw IllegalStateException if job not queued)
        LocalLauncher.startJob(jobContext, jobListener);

        return jobContext.getId();
    }

    /**
     * Interpret command line arguments.
     * @param args raw arguments string.
     * @param customArguments custom argument definition : map storing a boolean (true for a required value,
     *                        false for a simple flag) keyed by desired custom argument name).
     * @param customHelp arguments.
     * @return the map of parsed custom arguments keyed by argument names if any, null otherwise.
     */
    public static Map<String, String> interpretArguments(final String[] args, final Map<String, Boolean> customArguments,
            final String customHelp) {

        // List received arguments
        if (_logger.isDebugEnabled()) {
            for (int i = 0; i < args.length; i++) {
                _logger.debug("args[{}] = '{}'.", i, args[i]);
            }
        }

        // Just leave method if no argument has been given
        if (args == null) {
            return null;
        }

        // Define default arguments (help, version, log, open file)
        final List<LongOpt> longOpts = new ArrayList<LongOpt>();
        longOpts.clear();
        longOpts.add(new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'));
        longOpts.add(new LongOpt("version", LongOpt.NO_ARGUMENT, null, 1));
        longOpts.add(new LongOpt("loggui", LongOpt.NO_ARGUMENT, null, 2));
        longOpts.add(new LongOpt(CLI_OPEN_KEY, LongOpt.REQUIRED_ARGUMENT, null, 3));

        // In case the application needs custom arguments
        for (String argumentName : customArguments.keySet()) {
            final boolean hasArgument = customArguments.get(argumentName);
            final int requiredFlag = (hasArgument) ? LongOpt.REQUIRED_ARGUMENT : LongOpt.NO_ARGUMENT;
            longOpts.add(new LongOpt(argumentName, requiredFlag, null, 'c')); // 'c' means custom
        }

        // Instantiate the getopt object
        final LongOpt[] longOptArray = new LongOpt[longOpts.size()];
        longOpts.toArray(longOptArray);
        final ApplicationDescription applicationDescription = ApplicationDescription.getInstance();
        final Getopt getOpt = new Getopt(applicationDescription.getProgramName(), args, "hv:", longOptArray, true);

        /** Temporary store the command line arguments (long opt = value) */
        final Map<String, String> parsedArgumentValues = new LinkedHashMap<String, String>();
        int c; // argument key
        String arg; // argument value

        // While there is a argument key
        while ((c = getOpt.getopt()) != -1) {
            _logger.debug("opt = {}", c);

            switch (c) {
                // Show the arguments help
                case 'h':
                    showArgumentsHelp(customHelp);
                    break;

                // Show the name and the version of the program
                case 1:
                    // Show the application name on the shell
                    System.out.println(applicationDescription.getProgramNameWithVersion());

                    // Exit the application
                    Bootstrapper.stopApp(0);
                    break;

                // Display the LogGUI panel
                case 2:
                    LogbackGui.showLogConsole();
                    break;

                // Open the given file
                case 3:
                    // get the file path argument and store it temporarly :
                    final String fileArgument = getOpt.getOptarg();
                    if (fileArgument != null) {
                        _logger.info("Should open '{}'.", fileArgument);
                        parsedArgumentValues.put(CLI_OPEN_KEY, fileArgument);
                    }
                    break;

                // Set the logger level
                case 'v':
                    arg = getOpt.getOptarg();

                    if (arg != null) {
                        _logger.info("Set logger level to '{}'.", arg);

                        final ch.qos.logback.classic.Logger jmmcLogger = LoggingService.getJmmcLogger();
                        if (arg.equals("0")) {
                            jmmcLogger.setLevel(Level.OFF);
                        } else if (arg.equals("1")) {
                            jmmcLogger.setLevel(Level.ERROR);
                        } else if (arg.equals("2")) {
                            jmmcLogger.setLevel(Level.WARN);
                        } else if (arg.equals("3")) {
                            jmmcLogger.setLevel(Level.INFO);
                        } else if (arg.equals("4")) {
                            jmmcLogger.setLevel(Level.DEBUG);
                        } else if (arg.equals("5")) {
                            jmmcLogger.setLevel(Level.ALL);
                        } else {
                            showArgumentsHelp(customHelp);
                        }
                    }
                    break;

                // Show the arguments help
                case '?':
                    showArgumentsHelp(customHelp);
                    break;

                // Custom argument case
                case 'c':
                    final String name = longOpts.get(getOpt.getLongind()).getName();
                    final String value = (getOpt.getOptarg() != null) ? getOpt.getOptarg() : "";
                    parsedArgumentValues.put(name, value);
                    break;

                default:
                    System.out.println("Unknow command");

                    // Exit the application
                    Bootstrapper.stopApp(-1);
                    break;
            }
        }

        _logger.debug("Application arguments interpreted");
        return (parsedArgumentValues.size() > 0 ? parsedArgumentValues : null);
    }

    /** Show command arguments help. */
    private static void showArgumentsHelp(final String customHelp) {
        System.out.println("---------------------------------- Arguments help ------------------------------");
        System.out.println("| Key          Value           Description                                     |");
        System.out.println("|------------------------------------------------------------------------------|");
        System.out.println("| [-h]                         Show the options help                           |");
        System.out.println("| [-loggui]                    Show the logging tool                           |");
        System.out.println("| [-v]         [0|1|2|3|4|5]   Define console logging level                    |");
        System.out.println("| [-version]                   Show application name and version               |");
        System.out.println("| [-h|-help]                   Show arguments help                             |");
        System.out.println("|------------------------------------------------------------------------------|");

        // Print custom help if any
        if ((customHelp != null) && (!customHelp.isEmpty())) {
            System.out.println(customHelp);
        }

        System.out.println("LOG LEVELS : 0 = OFF, 1 = SEVERE, 2 = WARNING, 3 = INFO, 4 = FINE, 5 = ALL\n");

        // Exit the application
        Bootstrapper.stopApp(0);
    }

    /*
     private static String[] splitCliPath(final String cliPath) {

     System.out.println("cliPath = " + cliPath);

     final String delimiters = "'\" ";// + SystemUtils.PATH_SEPARATOR;
     System.out.println("delimiters = " + delimiters);

     final ArrayList<String> splittedPath = new ArrayList<String>();

     int tokenBeginning = -1;
     int tokenEnd;
     String currentToken;
     final int lenghtMinusOne = cliPath.length() - 1;
     for (int i = 0; i < lenghtMinusOne; i++) {

     // Fix the beginning of the current token if not yet defined
     if (tokenBeginning < 0) {
     tokenBeginning = i;
     // Jump directly to next char
     continue;
     }

     // If the current char is not a delimiter (and we are not reaching the end of the path)
     final String currentChar = cliPath.substring(i, i + 1);
     if ((!delimiters.contains(currentChar)) && (i < lenghtMinusOne - 1)) {
     // Skip it
     continue;
     }

     // Current char is delimiter, or we are reaching the end of the path !
     // BUG : what about embedded delimiters "" or '' strings ???

     // If previous char is an escape sequence
     final String previousChar = cliPath.substring(i - 1, i);
     if (previousChar.equals("\\")) { // Skip escaped delimiters
     // Skip current char
     // BUG : what about reaching the end of the path ???
     continue;
     }

     // Otherwise we are at the end of a token, so memorize it
     tokenEnd = i + 2;
     currentToken = cliPath.substring(tokenBeginning, tokenEnd);
     splittedPath.add(currentToken);
     tokenBeginning = -1;
     // Restart sequence from scratch at current position for next token
     }

     return splittedPath.toArray(new String[0]);
     }

     public static void main(String[] args) {
     String[] pathes = {"/usr/X11/bin/xeyes",
     "/Volumes/Backup\\ HD/Perso/Mac\\ OS\\ X\\ Install\\ DVD.dmg",
     "say \"Hello crual world !\"",
     "C:\\Program Files\\Inkscape\\inkscape.exe"};
     for (String string : pathes) {
     System.out.println("array = " + CollectionUtils.toString(splitCliPath(string)));
     }
     }
     */
    /** Forbidden constructor */
    private CommandLineUtils() {
    }
}

/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package fr.jmmc.jmcs.gui;

import fr.jmmc.jmcs.util.Urls;

import java.net.URL;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.Map;

/**
 * This class uses the JavaHelp system to show a
 * help window. The informations of the help window have
 * been taken from a file called <b>[module_name]-doc.jar</b>
 * located into the application lib folder and generated by
 * a bash script called <b>jmcsHTML2HelpSet.sh</b>.
 * The programs can ask to show one page.
 * @See ShowHelpAction
 * 
 * @author Sylvain LAFRASSE, Brice COLUCCI, Guillaume MELLA, Laurent BOURGES.
 */
public class HelpView {

    /** Logger */
    private static final Logger _logger = LoggerFactory.getLogger(HelpView.class.getName());
    /** internal reference to the help broker */
    private static HelpBroker _helpBroker;
    /** instance of help view */
    private static HelpView _instance = null;
    /** initialization flag */
    private static boolean _alreadyInitialized = false;

    /** Show the help window */
    private HelpView() {
        _instance = this;
    }

    /**
     * Tell if help set can be used
     *
     * @return true if the help set can be used, false otherwise.
     */
    public static boolean isAvailable() {
        if (_instance == null) {
            _instance = new HelpView();
        }

        if (_alreadyInitialized) {
            return true;
        }

        URL url = null;

        try {
            // Get the helpset file and create the centered help broker 
            url = HelpSet.findHelpSet(null, "documentation.hs");

            _logger.trace("HelpSet.findHelpSet(null, 'documentation.hs') = '{}'.", url);

            if (url == null) {
                url = HelpSet.findHelpSet(null, "/documentation.hs");

                _logger.trace("HelpSet.findHelpSet(null, '/documentation.hs') = '{}'.", url);
            }

            if (url == null) {
                // Works on Mac OS X 10.5 PPC G5 with JVM 1.5.0_16
                // Works on Mac OS X 10.5 Intel with JVM 1.5.0_16
                // Works on Windows XP with JVM 1.6.0_07
                // Works on Linux with JVM 1.5.0_16
                url = _instance.getClass().getClassLoader().getResource("documentation.hs");

                _logger.trace("_instance.getClass().getClassLoader().getResource('documentation.hs') = '{}'.", url);
            }

            if (url == null) {
                url = _instance.getClass().getClassLoader().getResource("/documentation.hs");

                _logger.trace("_instance.getClass().getClassLoader().getResource('/documentation.hs') = '{}'.", url);
            }

            // http://forums.sun.com/thread.jspa?messageID=10522645
            url = Urls.fixJarURL(url);

            _logger.debug("using helpset url = '{}'.", url);

            // check if the url is valid :
            if (url == null) {
                _logger.info("No helpset document found.");

                return false;
            }

            HelpSet helpSet = new HelpSet(_instance.getClass().getClassLoader(), url);
            _helpBroker = helpSet.createHelpBroker();
            _helpBroker.setLocation(WindowCenterer.getCenteringPoint(_helpBroker.getSize()));

        } catch (Exception ex) {
            // skip complex case
            _logger.error("Problem during helpset creation (url='{}', classloader={})",
                    new Object[]{url, _instance.getClass().getClassLoader(), ex});

            return false;
        }

        _alreadyInitialized = true;

        return true;
    }

    /**
     * Show or hide the help view depending on the value of parameter b.
     *
     * @param b if true, shows this component; otherwise, hides this componentShow or hide help view.
     */
    public static void setVisible(boolean b) {
        if (isAvailable()) {
            // Show the window
            _helpBroker.setDisplayed(b);
        }
    }

    /**
     * Return the first HelpID that ends with given label or null.
     * @param endOfHelpID label or anchor that must be used to search one helpID
     * @return null or full HelpId string
     */
    public static String getHelpID(String endOfHelpID) {
        //search helpId into map that ends with label
        Map m = _helpBroker.getHelpSet().getCombinedMap();
        Enumeration<?> e = m.getAllIDs();
        Map.ID id;

        while (e.hasMoreElements()) {
            id = (Map.ID) e.nextElement();

            if (id.getIDString().endsWith(endOfHelpID)) {
                return id.getIDString();
            }
        }

        return null;
    }

    /**
     * Ask the help view to show the page associated to the given helpID
     * @param helpID
     */
    public static void show(String helpID) {
        // show without move if it is already visible
        // one problem is still present : the window is not place in foreground
        // and can be hidden by other windows
        if (_helpBroker.isDisplayed()) {
            _helpBroker.setViewDisplayed(true);
        } else {
            _helpBroker.setDisplayed(true);
        }

        _helpBroker.setCurrentID(helpID);
    }
}
/*___oOo___*/

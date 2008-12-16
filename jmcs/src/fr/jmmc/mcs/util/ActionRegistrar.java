/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: ActionRegistrar.java,v 1.3 2008-09-05 08:33:52 lafrasse Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 * Revision 1.2  2008/09/04 15:58:00  lafrasse
 * Typo corrections.
 *
 * Revision 1.1  2008/09/04 15:47:29  lafrasse
 * First revision.
 *
 ******************************************************************************/
package fr.jmmc.mcs.util;

import java.util.Hashtable;
import java.util.logging.*;

import javax.swing.AbstractAction;


/**
 * ActionRegistrar singleton class.
 */
public class ActionRegistrar
{
    /** Logger */
    private static final Logger _logger = Logger.getLogger(
            "fr.jmmc.mcs.util.ActionRegistrar");

    /** Singleton instance */
    private static ActionRegistrar _instance = null;

    /** Preference Action unic identifying key */
    private static final String _preferenceActionKey = "preferenceActionKey";

    /** Quit Action unic identifying key */
    private static final String _quitActionKey = "quitActionKey";

    /**
     * Hastable to associate string keys like
     * "fr.jmmc.classpath.classname:fiedname" to AbstractAction instances.
     */
    private Hashtable<String, AbstractAction> _register = null;

    /** Hidden constructor */
    protected ActionRegistrar()
    {
        _register = new Hashtable<String, AbstractAction>();
    }

    /** Return the singleton instance */
    public static final synchronized ActionRegistrar getInstance()
    {
        // DO NOT MODIFY !!!
        if (_instance == null)
        {
            _instance = new ActionRegistrar();
        }

        return _instance;

        // DO NOT MODIFY !!!
    }

    /**
     * Register an action, its class and field name, in the registrar.
     *
     * @param classPath the path of the class containing the field pointing to
     * the action, in the form returned by 'getClass().getName();'.
     * @param fieldName the name of the field pointing to the action.
     * @param action the action instance to register.
     *
     * @return the previous registered action, null otherwise.
     */
    public AbstractAction put(String classPath, String fieldName,
        AbstractAction action)
    {
        _logger.entering("ActionRegistrar", "put");

        String         internalActionKey = classPath + ":" + fieldName;
        AbstractAction previousAction    = _register.put(internalActionKey,
                action);

        if (previousAction == null)
        {
            _logger.finest("Registered '" + internalActionKey +
                "' action for the first time.");
        }
        else if (previousAction != action)
        {
            _logger.warning("Overwritten the previously registered '" +
                internalActionKey + "' action.");
        }
        else
        {
            _logger.fine("Registered '" + internalActionKey +
                "' action succesfully.");
        }

        return previousAction;
    }

    /**
     * Return the previously registered action for the given class path and
     * field name.
     *
     * @param classPath the path of the class containing the field pointing to
     * the action, in the form returned by 'getClass().getName();'.
     * @param fieldName the name of the field pointing to the action.
     *
     * @return the retrieved registered action, null otherwise.
     */
    public AbstractAction get(String classPath, String fieldName)
    {
        _logger.entering("ActionRegistrar", "get");

        String         internalActionKey = classPath + ":" + fieldName;
        AbstractAction retrievedAction   = _register.get(internalActionKey);

        if (retrievedAction == null)
        {
            _logger.severe("Cannot find '" + internalActionKey + "' action.");
        }
        else
        {
            _logger.fine("Retrieved '" + internalActionKey +
                "' action succesfully.");
        }

        return retrievedAction;
    }

    /**
     * Register an action dedicated to handle Preference panel display.
     *
     * @param action the action instance to register.
     *
     * @return the previous registered action, null otherwise.
     */
    public AbstractAction putPreferenceAction(AbstractAction action)
    {
        _logger.entering("ActionRegistrar", "putPreferenceAction");

        return _register.put(_preferenceActionKey, action);
    }

    /**
     * Return the previously registered action dedicated to Preference panel
     * display handling.
     *
     * @return the retrieved registered action, null otherwise.
     */
    public AbstractAction getPreferenceAction()
    {
        _logger.entering("ActionRegistrar", "getPreferenceAction");

        return _register.get(_preferenceActionKey);
    }

    /**
     * Register an action dedicated to handle Quit sequence.
     *
     * @param action the action instance to register.
     *
     * @return the previous registered action, null otherwise.
     */
    public AbstractAction putQuitAction(AbstractAction action)
    {
        _logger.entering("ActionRegistrar", "putQuitAction");

        return _register.put(_quitActionKey, action);
    }

    /**
     * Return the previously registered action dedicated to Quit sequence
     * handling.
     *
     * @return the retrieved registered action, null otherwise.
     */
    public AbstractAction getQuitAction()
    {
        _logger.entering("ActionRegistrar", "getQuitAction");

        return _register.get(_quitActionKey);
    }

    /**
     * Serialize the registrar content for output.
     *
     * @return the registrar content as a String.
     */
    public String toString()
    {
        _logger.entering("ActionRegistrar", "toString");

        return _register.toString();
    }
}
/*___oOo___*/

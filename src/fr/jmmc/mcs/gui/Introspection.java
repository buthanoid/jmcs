/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: Introspection.java,v 1.7 2011-04-04 16:13:33 bourgesl Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 * Revision 1.6  2008/09/02 12:31:00  lafrasse
 * Code, documentation and logging cleanup.
 *
 * Revision 1.5  2008/09/01 11:07:45  lafrasse
 * Improved logging.
 *
 * Revision 1.4  2008/06/25 08:12:22  bcolucci
 * Add functions.
 *
 * Revision 1.3  2008/06/20 08:41:45  bcolucci
 * Remove unused imports and add class comments.
 *
 * Revision 1.2  2008/06/12 11:54:11  bcolucci
 * Add functions in order to simply execute a method without get it's value.
 *
 * Revision 1.1  2008/06/12 09:30:06  bcolucci
 * *** empty log message ***
 *
 ******************************************************************************/
package fr.jmmc.mcs.gui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides helper functions related to object introspection.
 *
 * For example, you can easily execute a method of a class given the path to the
 * seeked class. and the method name. Or you can also discover at runtime if a
 * field exists, retireve a method execution result, etc ...
 */
public class Introspection
{

    /** Logger */
    private static final Logger _logger = Logger.getLogger(Introspection.class.getName());
    /** empty class array */
    private final static Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[]{};
    /** empty object array */
    private final static Object[] EMPTY_OBJECT_ARRAY = new Object[]{};

    /**
     * Returns a class object according to a given class path.
     *
     * @param classPath path to the seeked class.
     *
     * @return found class, null otherwise.
     */
    public static Class<?> getClass(final String classPath)
    {
        Class<?> searchedClass = null;

        try {
            searchedClass = Class.forName(classPath);
        } catch (ClassNotFoundException cnfe) {
            _logger.log(Level.WARNING, "Cannot find class '" + classPath + "'", cnfe);
        }

        return searchedClass;
    }

    /**
     * Test whether a class exists, from a given class path.
     *
     * @param classPath path to the seeked class.
     *
     * @return true if the class exists, false otherwise.
     */
    public static boolean hasClass(final String classPath)
    {
        if (getClass(classPath) != null) {
            if (_logger.isLoggable(Level.FINE)) {
                _logger.fine("Found class '" + classPath + "'.");
            }

            return true;
        }

        return false;
    }

    /**
     * Returns the package containing the class identified by the given class
     * path.
     *
     * @param classPath path to the seeked class.
     *
     * @return found class package, null otherwise.
     */
    public static Package getClassPackage(final String classPath)
    {
        final Package packageSearched = getClass(classPath).getPackage();

        return packageSearched;
    }

    /**
     * Returns the package containing the given class.
     *
     * @param seekedClass class.
     *
     * @return found class package, null otherwise.
     */
    public static Package getClassPackage(final Class<?> seekedClass)
    {
        return seekedClass.getPackage();
    }

    /**
     * Returns the name of the package containing the class identified by the
     * given class path.
     *
     * @param classPath path to the seeked class.
     *
     * @return found class package name, null otherwise.
     */
    public static String getClassPackageName(final String classPath)
    {
        return getClassPackage(classPath).getName();
    }

    /**
     * Returns the name of the package containing the given class.
     *
     * @param seekedClass class.
     *
     * @return found class package name, null otherwise.
     */
    public static String getClassPackageName(final Class<?> seekedClass)
    {
        return seekedClass.getPackage().getName();
    }

    /**
     * Returns a new instance of the class identified by the gien class path.
     *
     * @param classPath path to the seeked class.
     *
     * @return new instance of the class, null otherwise.
     */
    public static Object getInstance(final String classPath)
    {
        Object instance = null;

        try {
            instance = getClass(classPath).newInstance();
        } catch (InstantiationException ie) {
            _logger.log(Level.WARNING, "Cannot get instance of class '" + classPath + "'", ie);
        } catch (IllegalAccessException iae) {
            _logger.log(Level.WARNING, "Cannot get instance of class '" + classPath + "'", iae);
        }

        return instance;
    }

    /**
     * Returns the list of methods of the class identified by the given class
     * path.
     *
     * @param classPath path to the seeked class.
     *
     * @return Array of found methods, null otherwise.
     */
    public static Method[] getMethods(final String classPath)
    {
        return getClass(classPath).getMethods();
    }

    /**
     * Returns the seeked method with EMPTY argument list in the class
     * identified by the given class path.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     *
     * @return seeked method in class, null otherwise.
     */
    public static Method getMethod(final String classPath, final String methodName)
    {
        return getMethod(classPath, methodName, EMPTY_CLASS_ARRAY);
    }

    /**
     * Returns the seeked method with given argument list in the class
     * identified by the given class path.
     *
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     * @param parameters seeked method parameter Class array.
     *
     * @return seeked method with parameters in class, null otherwise.
     */
    public static Method getMethod(final String classPath, final String methodName,
            final Class<?>[] parameters)
    {
        Method methodSearched = null;
        try {
            methodSearched = getClass(classPath).getMethod(methodName, parameters);
        } catch (NoSuchMethodException nsme) {
            _logger.log(Level.WARNING, "Cannot find method '" + methodName + " of class '" + classPath + "'", nsme);
        }

        return methodSearched;
    }

    /**
     * Test whether a method exists, from a given class path.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     *
     * @return true if the method exists, false otherwise.
     */
    public static boolean hasMethod(final String classPath, final String methodName)
    {
        return hasMethod(classPath, methodName, EMPTY_CLASS_ARRAY);
    }

    /**
     * Test whether a method with guven parameters list exists, from a given
     * class path.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     * @param parameters seeked parameters array.
     *
     * @return true if the method exists, false otherwise.
     */
    public static boolean hasMethod(final String classPath, final String methodName,
            final Class<?>[] parameters)
    {
        if (getMethod(classPath, methodName, parameters) != null) {
            if (_logger.isLoggable(Level.FINE)) {
                _logger.fine("Found method '" + methodName + "' in class '"
                        + classPath + "'.");
            }

            return true;
        }

        return false;
    }

    /**
     * Returns the execution result of the method in the class identified by
     * their own names.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     *
     * @return result of the method execution, null otherwise.
     */
    public static Object getMethodValue(final String classPath, final String methodName)
    {
        return getMethodValue(classPath, methodName, EMPTY_CLASS_ARRAY);
    }

    /**
     * Returns the execution result of the method with given parameters in the
     * class identified by their own names.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     * @param parameters parameters array.
     *
     * @return result of the method execution, null otherwise.
     */
    public static Object getMethodValue(final String classPath, final String methodName,
            final Class<?>[] parameters)
    {
        return getMethodValue(classPath, methodName, parameters, EMPTY_OBJECT_ARRAY);
    }

    /**
     * Returns the execution result of the method with given parameters and
     * argument values in the class identified by their own names.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     * @param parameters parameters array.
     * @param arguments arguments array.
     *
     * @return result of the method execution, null otherwise.
     */
    public static Object getMethodValue(final String classPath, final String methodName,
            final Class<?>[] parameters, final Object[] arguments)
    {
        final Method method = getMethod(classPath, methodName, parameters);

        return getMethodValue(method, getInstance(classPath), arguments);
    }

    /**
     * Returns the execution result of the method (no-args) with the given class instance.
     *
     * @param method method to invoke.
     * @param instance class instance to use
     *
     * @return result of the method execution, null otherwise.
     */
    public static Object getMethodValue(final Method method, final Object instance)
    {
        return getMethodValue(method, instance, EMPTY_OBJECT_ARRAY);
    }

    /**
     * Returns the execution result of the method with given argument values (static method).
     *
     * @param method method to invoke.
     * @param arguments arguments array.
     *
     * @return result of the method execution, null otherwise.
     */
    public static Object getMethodValue(final Method method, final Object[] arguments)
    {
        return getMethodValue(method, null, arguments);
    }

    /**
     * Returns the execution result of the method with given argument values.
     *
     * @param method method to invoke.
     * @param instance class instance to use
     * @param arguments arguments array.
     *
     * @return result of the method execution, null otherwise.
     */
    public static Object getMethodValue(final Method method, final Object instance, final Object[] arguments)
    {
        Object value = null;

        try {
            value = method.invoke(instance, arguments);
        } catch (IllegalAccessException iae) {
            _logger.log(Level.WARNING, "Cannot get result of method '" + method.getName() + "'", iae);
        } catch (IllegalArgumentException iae) {
            _logger.log(Level.WARNING, "Cannot get result of method '" + method.getName() + "'", iae);
        } catch (InvocationTargetException ite) {
            _logger.log(Level.WARNING, "Cannot get result of method '" + method.getName() + "'", ite);
        }

        return value;
    }

    /**
     * Execute a method in the class identified by their own names, without
     * returning the result.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     */
    public static void executeMethod(final String classPath, final String methodName)
    {
        executeMethod(classPath, methodName, EMPTY_CLASS_ARRAY);
    }

    /**
     * Execute a method with given parameters in the class identified by their
     * own names, without returning the result.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     * @param parameters parameters array.
     */
    public static void executeMethod(final String classPath, final String methodName,
            final Class<?>[] parameters)
    {
        executeMethod(classPath, methodName, parameters, EMPTY_OBJECT_ARRAY);
    }

    /**
     * Execute a method with given parameters and argument values in the class
     * identified by their own names, without returning the result.
     *
     * @param classPath path to the seeked class.
     * @param methodName seeked method name.
     * @param parameters parameters array.
     * @param arguments arguments array.
     */
    public static void executeMethod(final String classPath, final String methodName,
            final Class<?>[] parameters, final Object[] arguments)
    {
        getMethodValue(classPath, methodName, parameters, arguments);
    }

    /**
     * Returns the list of fields of the class identified by the given class
     * path.
     *
     * @param classPath path to the seeked class.
     *
     * @return Array of found fields, null otherwise.
     */
    public static Field[] getFields(final String classPath)
    {
        return getClass(classPath).getFields();
    }

    /**
     * Returns the seeked field in the class identified by the given class path.
     *
     * @param classPath path to the seeked class.
     * @param fieldName seeked field name.
     *
     * @return seeked field in class, null otherwise.
     */
    public static Field getField(final String classPath, final String fieldName)
    {
        Field field = null;

        try {
            field = getClass(classPath).getField(fieldName);
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "Cannot get field '" + fieldName + "'", ex);
        }

        return field;
    }

    /**
     * Test whether a field exists, from a given class path.
     *
     * @param classPath path to the seeked class.
     * @param fieldName seeked field name.
     *
     * @return true if the field exists, false otherwise.
     */
    public static boolean hasField(final String classPath, final String fieldName)
    {
        if (getField(classPath, fieldName) != null) {
            if (_logger.isLoggable(Level.FINE)) {
                _logger.fine("Found field '" + fieldName + "' in class '"
                        + classPath + "'.");
            }

            return true;
        }

        return false;
    }

    /**
     * Returns the value of the field in the class identified by the given class
     * path.
     *
     * @param classPath seeked class name.
     * @param fieldName seeked field name.
     *
     * @return value of the seeked field, null otherwise.
     */
    public static Object getFieldValue(final String classPath, final String fieldName)
    {
        final Field field = getField(classPath, fieldName);
        Object value = null;

        try {
            value = field.get(getInstance(classPath));
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "Cannot get value of field '" + fieldName + "'", ex);
        }

        return value;
    }

    /**
     * Private constructor
     */
    private Introspection()
    {
        super();
    }
}
/*___oOo___*/

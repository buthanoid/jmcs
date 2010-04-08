/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: ALX.java,v 1.17 2010-04-08 08:40:50 bourgesl Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 * Revision 1.16  2010/02/18 11:02:30  mella
 * add logg and teff to the ld2ud outputs
 *
 * Revision 1.15  2010/01/20 13:58:31  mella
 * add javadoc
 *
 * Revision 1.14  2010/01/20 13:56:12  mella
 * remove duplicated code
 *
 * Revision 1.13  2010/01/11 22:06:33  mella
 * fix formulae to get ld from ud
 *
 * Revision 1.12  2010/01/08 16:22:22  mella
 * add most material to compute ld to ud
 *
 * Revision 1.11  2010/01/07 10:21:07  mella
 * Add first prototype version of ld2ud program
 *
 * Revision 1.10  2009/12/01 14:19:18  lafrasse
 * Corrected spectralType() to ignore 'SB' as 'S' and 'B'  spectral type tokens (Feedback Report ID : #1259360028).
 *
 * Revision 1.9  2009/11/27 15:57:16  lafrasse
 * Jalopization.
 *
 * Revision 1.8  2009/11/25 08:28:02  mella
 * Add executable feature
 *
 * Revision 1.7  2009/11/02 12:05:42  lafrasse
 * Added angle conversion constants.
 *
 * Revision 1.6  2009/10/20 12:38:45  lafrasse
 * Added conversion from degrees to minutes and vice et versa.
 *
 * Revision 1.5  2008/05/30 12:31:11  lafrasse
 * Changed convertRA() & convertDEC() APIs to parseRA() & parseDEC() respectively.
 * Added methods for "arcmin to/from degrees and minutes" conversion.
 *
 * Revision 1.4  2007/06/21 07:38:51  lafrasse
 * Jalopization.
 *
 * Revision 1.3  2007/05/16 14:34:43  lafrasse
 * Removed the dependency on the Java 1.5 'Scanner' class.
 *
 * Revision 1.2  2007/02/13 13:48:51  lafrasse
 * Moved sources from sclgui/src/jmmc into jmcs/src/fr and rename packages
 *
 * Revision 1.1  2006/11/14 14:41:57  lafrasse
 * Creation.
 *
 ******************************************************************************/
package fr.jmmc.mcs.astro;

import cds.astro.Sptype;
import fr.jmmc.mcs.astro.star.Star;
import fr.jmmc.mcs.astro.star.Star.Property;
import fr.jmmc.mcs.log.MCSLogger;

import java.text.ParseException;
import java.util.*;


/**
 * Astronomical Library Extension.
 *
 * Class regrouping usefull statics method to convert star coordinates between
 * different formats and units.
 */
public class ALX
{
    /** Describe the micrometer (micron, or um) unit */
    public static final double MICRON = 1.0;

    /** Describe the meter unit */
    public static final double METER = 1.0;

    /** Describe the arcminute unit */
    public static final double ARCMIN = 1.0;

    /** Specify the value of one arcminute in degrees */
    public static final double ARCMIN_IN_DEGREES = (1.0 / 60.0);

    /** Describe the arcsecond unit */
    public static final double ARCSEC = 1.0;

    /** Specify the value of one arcsecond in degrees */
    public static final double ARCSEC_IN_DEGREES = (1.0 / 3600.0);

    /**
     * Convert the given Right Ascension (RA).
     *
     * @param raHms the right ascension as a HH:MM:SS.TT or HH MM SS.TT string.
     *
     * @return the right ascension as a double in degrees.
     */
    public static double parseHMS(String raHms)
    {
        double hh;
        double hm;
        double hs;

        // RA can be given as HH:MM:SS.TT or HH MM SS.TT. 
        // Replace ':' by ' ', and remove trailing and leading pace
        raHms     = raHms.replace(':', ' ');
        raHms     = raHms.trim();

        // Parse the given string
        try
        {
            String[] tokens = raHms.split(" ");
            hh     = Double.parseDouble(tokens[0]);
            hm     = Double.parseDouble(tokens[1]);
            hs     = Double.parseDouble(tokens[2]);
        }
        catch (Exception e)
        {
            hh     = 0.0;
            hm     = 0.0;
            hs     = 0.0;
        }

        // Get sign of hh which has to be propagated to hm and hs
        final double sign = (raHms.startsWith("-")) ? -1d : 1d;

        // Convert to degrees
        // note : dd already includes the sign :
        final double ra = (hh + sign * (hm / 60d + hs / 3600d)) * 15d;

        MCSLogger.debug("HMS  : ’" + raHms + "' = '" + ra + "'.");

        return ra;
    }


    /**
     * Convert the given Right Ascension (RA).
     *
     * @param raHms the right ascension as a HH:MM:SS.TT or HH MM SS.TT string.
     *
     * @return the right ascension as a double in degrees  [-180 - 180].
     */
    public static double parseRA(String raHms)
    {
        double ra = parseHMS(raHms);

        // Set angle range [-180 - 180]
        if (ra > 180d)
        {
            ra = -1d * (360d - ra);
        }

        MCSLogger.debug("RA  : ’" + raHms + "' = '" + ra + "'.");

        return ra;
    }

    /**
     * Convert the given Declinaison (DEC).
     *
     * @param decDms the declinaison as a DD:MM:SS.TT or DD MM SS.TT string.
     *
     * @return the declinaison as a double in degrees.
     */
    public static double parseDEC(String decDms)
    {
        double dd;
        double dm;
        double ds;

        // DEC can be given as DD:MM:SS.TT or DD MM SS.TT. 
        // Replace ':' by ' ', and remove trailing and leading pace
        decDms     = decDms.replace(':', ' ');
        decDms     = decDms.trim();

        // Parse the given string
        try
        {
            String[] tokens = decDms.split(" ");
            dd     = Double.parseDouble(tokens[0]);
            dm     = Double.parseDouble(tokens[1]);
            ds     = Double.parseDouble(tokens[2]);
        }
        catch (Exception e)
        {
            dd     = 0.0;
            dm     = 0.0;
            ds     = 0.0;
        }

        // Get sign of dd which has to be propagated to dm and ds
        final double sign = (decDms.startsWith("-")) ? -1d : 1d;

        // Convert to degrees
        // note : dd already includes the sign :
        double dec = dd + sign * (dm / 60d + ds / 3600d);

        MCSLogger.debug("DEC : ’" + decDms + "' = '" + dec + "'.");

        return dec;
    }

    /**
     * Extract one or more spectral types of the given spectral type.
     *
     * @param rawSpectralType the spectral type to analyze.
     *
     * @return a Vector of String containing found spectral types (if any).
     */
    public static Vector spectralTypes(String rawSpectralType)
    {
        // Remove any "SB" token (Feedback Report ID : #1259360028)
        if (rawSpectralType.contains("SB"))
        {
            rawSpectralType = rawSpectralType.replaceAll("SB", "");
        }

        Vector foundSpectralTypes = new Vector();

        for (int i = 0; i < rawSpectralType.length(); i++)
        {
            char c = rawSpectralType.charAt(i);

            // If the luminosity class has been reached
            if ((c == 'I') || (c == 'V'))
            {
                // Skip those characters
                continue;
            }

            // If the spectral type has been reached
            // eg. the uppercase alphabetic parts of a spectral type
            if ((Character.isLetter(c) == true) &&
                    (Character.isUpperCase(c) == true))
            {
                // Re-copy its content for later use (as a String object)
                foundSpectralTypes.add("" + c);
            }
        }

        return foundSpectralTypes;
    }

    /**
     * Extract one or more luminosity classes of the given spectral type.
     *
     * @param rawSpectralType the spectral type to analyze.
     *
     * @return a Vector of String containing found luminosity classes (if any).
     */
    public static Vector luminosityClasses(String rawSpectralType)
    {
        MCSLogger.trace();

        Vector  foundLuminosityClasses = new Vector();
        String  foundLuminosityClass   = "";
        boolean luminosityClassFound   = false;

        int     rawSpectralTypeSize    = rawSpectralType.length();

        // Scan every given spectral type characters
        for (int i = 0; i < rawSpectralTypeSize; i++)
        {
            char c = rawSpectralType.charAt(i);

            // If a luminosity class has been reached
            // eg. a part of a spectral type composed of I & V (roman numbers)
            if ((c == 'I') || (c == 'V'))
            {
                // Re-copy its content to build a result string
                foundLuminosityClass     = foundLuminosityClass + c;

                // Mark the discovery
                luminosityClassFound     = true;

                // If we are on the last char of the spectral type
                if (i == (rawSpectralTypeSize - 1))
                {
                    // Store the luminosity class as a result
                    foundLuminosityClasses.add(foundLuminosityClass);
                }
            }
            else
            {
                // if a luminosiy class was just entirely found
                if (luminosityClassFound == true)
                {
                    // Store the luminosity class as a result
                    foundLuminosityClasses.add(foundLuminosityClass);

                    // Reset in case another luminosity class can be found
                    foundLuminosityClass     = "";
                    luminosityClassFound     = false;
                }
            }
        }

        return foundLuminosityClasses;
    }

    /**
     * Convert a value in arc-minute to minutes.
     *
     * @param arcmin the arc-minute value to convert.
     *
     * @return a double containing the converted value.
     */
    public static double arcmin2minutes(double arcmin)
    {
        double minutes = (arcmin / 15);

        return minutes;
    }

    /**
     * Convert a value in minutes to arc-minute.
     *
     * @param minutes the value in minutes to convert.
     *
     * @return a double containing the converted value.
     */
    public static double minutes2arcmin(double minutes)
    {
        double arcmin = (minutes * 15);

        return arcmin;
    }

    /**
     * Convert a value in arc-minute to degrees.
     *
     * @param arcmin the arc-minute value to convert.
     *
     * @return a double containing the converted value.
     */
    public static double arcmin2degrees(double arcmin)
    {
        double degrees = (arcmin / 60);

        return degrees;
    }

    /**
     * Convert a value in degrees to arc-minute.
     *
     * @param degrees the value in degrees to convert.
     *
     * @return a double containing the converted value.
     */
    public static double degrees2arcmin(double degrees)
    {
        double arcmin = (degrees * 60);

        return arcmin;
    }

    /**
     * Convert a minute value to degrees.
     *
     * @param minutes the value in minute to convert.
     *
     * @return a double containing the converted value.
     */
    public static double minutes2degrees(double minutes)
    {
        double degrees = minutes / 4;

        return degrees;
    }

    /**
     * Convert a value in degrees to minute.
     *
     * @param degrees the value in degrees to convert.
     *
     * @return a double containing the converted value.
     */
    public static double degrees2minutes(double degrees)
    {
        double minutes = degrees * 4;

        return minutes;
    }

    /** 
     * ld should be a diamvk.
     */
    /**
     * Compute teff and logg from given spectral type and return a star
     * with Uniform diameters properties computed from the nearest
     * teff and logg found in the various tables .
     * @param ld
     * @param sptype
     * @return a Star with UD properties.
     * @throws ParseException
     */
    public static Star ld2ud(double ld, String sptype) throws ParseException{
        double teff = LD2UD.getEffectiveTemperature(sptype);
        double logg = LD2UD.getGravity(sptype);
        return ld2ud(ld,teff,logg);
    }

    /**
     * Return a star with Uniform diameters properties computed from the nearest
     * teff and logg found in the various tables.
     *
     * @param ld should be a diamvk.
     * @param teff
     * @param logg
     * @return a Star with UD properties.     
     */
    public static Star ld2ud(double ld, double teff, double logg){
        Star star = new Star();
        star.setPropertyAsDouble(Property.TEFF, teff);
        star.setPropertyAsDouble(Property.LOGG, logg);
        Property [] uds = new Property[]{
          Property.UD_B, Property.UD_I, Property.UD_J, Property.UD_H, Property.UD_K ,
          Property.UD_L, Property.UD_N, Property.UD_R, Property.UD_U, Property.UD_V
        };

        for (Property ud : uds) {
          double diam = ld / LD2UD.getLimbDarkenedCorrectionFactor(ud, teff, logg);
          star.setPropertyAsDouble(ud,diam);
        }
        return star;
    }

    public static int getTemperatureClass(String spectype) throws ParseException{
      Sptype sp = new Sptype(spectype);
      String spNum = sp.getSpNumeric();
      int firstDotIndex = spNum.indexOf(".");
      return Integer.parseInt(spNum.substring(0, firstDotIndex));
    }

    public static int getLuminosityClass(String spectype) throws ParseException{
      Sptype sp = new Sptype(spectype);
      String spNum = sp.getSpNumeric();
      int firstDotIndex = spNum.indexOf(".");
      int secondDotIndex = spNum.lastIndexOf(".");
      return Integer.parseInt(spNum.substring(firstDotIndex+1,secondDotIndex));
    }


    /**
     * Set this class with limited executable features.
     *
     * The user can use it giving one method name and argument value
     *  e.g.:
     *  ALX parseRA "1:1:1"
     *  ALX spectralTypes "M1/M2/IV/III"
     *  ALX luminosityClasses "M1/M2/IV/III"
     *
     * If no argument is given, then it prints out the usage form.
     */
    public static void main(String[] args)
    {
        Class c = null;

        try
        {
            c = Class.forName(ALX.class.getName());

            String                   method     = args[0];
            String                   arg        = args[1];

            java.lang.reflect.Method userMethod = c.getMethod(method,
                    String.class);
            System.out.println("" + userMethod.invoke(arg, arg));
        }
        catch (Throwable e)
        {
            java.lang.reflect.Method[] m = c.getDeclaredMethods();
            System.out.println("Usage: <progname> <methodName> <arg>");
            System.out.println("     where <methodName> can be:");

            for (int i = 0; i < m.length; i++)
            {
                System.out.println("       - " + m[i].getName());
            }
        }
    }
}
/*___oOo___*/

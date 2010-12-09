package org.ossnipes.snipes.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/** Snipes configuration utilities. This class cannot be dirived from, and cannot be a 
 * instance object.
 * @author Jack McCracken (Auv5)
 */
public final class Configuration {

    private static Properties p;
    /* Please note: Any modifications that explicitly require a FileInputStream
     * should use fis, and all explicitly requiring a FileOutputStream should use
     * fos, as these are garenteed to be open InputStreams and OutputStreams to
     * the configuration file
     */
    private static FileInputStream fis = null;
    private static FileOutputStream fos = null;
    // Static block, made sure everything is set.

    static {
        if (!new File(Constants.CONFNAME).exists()) {
            System.err.println("The configuration file does not exist. Please "
                    + "Move the \"example.properties\" file that came with Snipes"
                    + " to " + Constants.CONFNAME);
            System.exit(1);
        }

        p = new Properties();
        try {
            fis = new FileInputStream(Constants.CONFNAME);
            fos = new FileOutputStream(Constants.CONFNAME);
        } catch (Exception e) {
            System.err.println("A " + e.getClass().getCanonicalName()
                    + " has occured. It's message was: " + e.getMessage()
                    + ". Suggestions: Make sure it exists, if not use"
                    + " example.properties that came with Snipes and"
                    + " move it to \" " + Constants.CONFNAME + "\". Quitting...");
            System.exit(1);
        }
        try {
            p.load(fis);
        } catch (IOException e) {
            System.err.println("An unknown " + e.getClass().getCanonicalName()
                    + " has occurred. It's message was: " + e.getMessage()
                    + ". Suggestions: Make sure it exists, if not use"
                    + " example.properties that came with Snipes and"
                    + " move it to \" " + Constants.CONFNAME + "\". Quitting...");
            System.exit(1);
        }
    }

    /** This class cannot be make a instance object. */
    private Configuration() {
    }

    /** Looks up a property in the configuration file and returns it.
     * @param key The "key" of the configuration directive
     * @return The value of the property if found, null if not. See lookUp(String key, String defaultValue) for default value behaviour.
     */
    public static String lookUp(String key) {
        return lookUp(key, null);
    }

    /** Looks up a property in the configuration file and returns it. If it is
     * not found, returns defaultValue.
     * @param key The "key" of the configuration directive.
     * @param defaultValue The value to return if it is not found.
     * @return The value of the property if it was found, defaultValue if not.
     */
    public static String lookUp(String key, String defaultValue) {
        return p.getProperty(key, defaultValue);
    }

    /** Splits the value of the given key by the regular expression provided.
     * @param key The key to get.
     * @param regex The regular expression to split by.
     * @param defaultValue The default value to use if the key is not found
     * @param useTrimming If we should trim the resulting String array of trailing whitespace
     * @return The value of the key, split by the specified regular expression if the key exists. defaultValue otherwise.
     */
    public static String[] getSplitProperty(String key, String defaultValue, String regex, boolean useTrimming) {
        String lookup = lookUp(key, defaultValue);
        if (lookup == null) {
            return null;
        }
        String[] firstOff = lookup.split(regex);
        if (useTrimming) {
            for (int i = 0; i < firstOff.length; i++) {
                firstOff[i] = firstOff[i].trim();
            }
        }
        return firstOff;
    }

    /** Splits the value of the given key by the regex provided. Defaults to
     * using trunnication of trailing whitespace in resulting array.
     * @param key The key to get.
     * @param regex The regular expression to split by.
     * @param defaultValue The default value to use if the key is not found
     * @return The value of the key, split by the specified regex if the key exists. defaultValue otherwise.
     */
    public static String[] getSplitProperty(String key, String defaultValue, String regex) {
        return getSplitProperty(key, defaultValue, regex, true);
    }

    /** Splits the value of the given key by the regex provided. Defaults to
     * using trunnication of trailing whitespace in resulting array.
     * @param key The key to get
     * @param regex The regular expression to split by
     * @return The value of the key, split by the specified regex if the key exists. Null otherwise.
     */
    public static String[] getSplitProperty(String key, String regex) {
        return getSplitProperty(key, null, regex, true);
    }

    /** Splits the value of the given key by regular expression ","
     * (splits into a array divided by the ,s in the key String).
     * Defaults to using trunnication of trailing whitespace in resulting array.
     * @param key The key to get.
     * @return The value of the key, split by the regex "," if the key exists. Null otherwise.
     */
    public static String[] getSplitProperty(String key) {
        return getSplitProperty(key, null, ",", true);
    }

    /** Sets the given property to the given value, and saves the configuration.
     * @param key The key to set.
     * @param value The value to set the key to.
     */
    public static void setProperty(String key, String value) {
        setProperty(key, value, true);
    }

    /** Sets the given property to the given value, and will
     * save it if 'boolean save' is true.
     * @param key The key to set.
     * @param value The value to set it to.
     * @param save If we should save the configuraiton.
     */
    public static void setProperty(String key, String value, boolean save) {
        p.setProperty(key, value);
        if (save) {
            writeConfiguration();
        }
    }

    /** Saves the configuration properties file */
    public static void writeConfiguration() {
        try {
            writeConfiguration(fos, "");
        } catch (Exception unhandled) {
            /* Unreachable catch block, Snipes exits when it sees that the
             *  configuration does not exist or cannot be accessed.
             */
        }
    }

    /** Writes the configuration file to the specified output stream.
     * This may be useful for transmitting configuration over the network.
     * @param out The output stream to write to.
     */
    public static void writeConfiguration(OutputStream out) {
        writeConfiguration(out, "");
    }

    /** Writes the configuration file to the specified output stream with the
     * specified comments at the top.
     * @param out The output stream to write to.
     * @param comments The comments to write.
     */
    public static void writeConfiguration(OutputStream out, String comments) {
        try {
            p.store(out, comments);
        } catch (Exception unhandled) {
            /* Unreachable catch block, Snipes exits when it sees that the
             *  configuration does not exist or cannot be accessed.
             */
        }
    }

    /** Reloads the configuration file from disk. May delete non-saved changes.
     * @throws IOException */
    public static void reloadConfiguration() throws IOException {
        p.load(fis);
    }
    

    /** Closes all the input streams, throwing any Throwables. <b>ONLY TO BE CALLED
     * FROM CleanUp AT FINISH!!!</b> Don't call me!!
     * @throws IOException Shouldn't matter, as it's only called in CleanUp! But, for completeness, if the FileStreams fail to close, it throws the exception.
     */
    public static void finish() throws IOException {
        fos.close();
        fis.close();
        writeConfiguration();
    }
}

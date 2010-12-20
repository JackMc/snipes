package org.ossnipes.snipes.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
            fos = new FileOutputStream(Constants.CONFNAME,true);
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
        try {
            readConfigurationComments();
        } catch (IOException ex) {
            System.err.println("The comments in your configuration file could not be read."
                    + " Your configuration file will just come out without the comments it"
                    + " was read in with. I would suggest backing up your configuration file"
                    + " if comments really matter to you.");
        }
    }
    private static Map<Integer,String> comments;

    /** Attempts to read the comments from the configuration file. This is sort of
     * a hack to preserve our comments in the configuration file. By the time I had realized
     * that properties.store destroyed comments, I didn't have time to reimplement configuration.
     * This method will not show up in JavaDocs, as it's private.
     * @throws IOException If we can't read from the configuration. Considering something like
     * this is caught above this method being called, this shouldn't happen.
     */
    private static void readConfigurationComments() throws IOException
    {
        if (comments == null)
        {
            comments = new HashMap<Integer,String>();
        }
        if (!comments.isEmpty())
        {
            comments.clear();
        }
        LineNumberReader br = new LineNumberReader(new InputStreamReader(fis));

        String line;
        while ((line = br.readLine()) != null)
        {
            if (!line.startsWith("#"))
            {
                continue;
            }
            comments.put(new Integer(br.getLineNumber()), line);
        }
    }
    /** This class cannot be made a instance object. */
    private Configuration() {}

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
     * @return The value of the key, split by the specified regular expression if the key exists. Uses defaultValue as the first element in the array.
     */
    public static String[] getSplitProperty(String key, String defaultValue, String regex, boolean useTrimming) {
        String lookup;
        if (defaultValue == null)
        {
            lookup = lookUp(key);
        }
        else
        {
            lookup = lookUp(key,defaultValue);
        }
        if (lookup == null) {
            return null;
        }
        if (lookup.equals(defaultValue) && defaultValue.equals(""))
        {
            return new String [] {};
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
            writeConfiguration(fos);
    }

    /** Writes the configuration file to the specified output stream.
     * @param out The output stream to write to.
     */
    public static void writeConfiguration(OutputStream out) {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            int line = 0;
            String comment = comments.get(new Integer(line));
            if (comment != null)
            {
                bw.write(comment);
                bw.newLine();
                line ++;
            }
            Enumeration<Object> keys = p.keys();
            int totalLines = p.keySet().size() + comments.size() + (comment==null?0:1);

            for (;line<totalLines;line ++)
            {
                String cc = comments.get(new Integer(line));
                if (cc != null)
                {
                    bw.write(cc);
                }
                else
                {
                    Object o = keys.nextElement();
                    if (o instanceof String)
                    {
                        String s = (String)o;
                        bw.write(s);
                    }
                }
                bw.newLine();
            }
        } catch (IOException e) {System.err.println("An error has occured while writing the configuration to disk. This theoretically should not happen, as it is checked at init.");}
    }

    /** Reloads the configuration file from disk. May delete non-saved in-bot changes.
     * @throws IOException
     */
    public static void reloadConfiguration() throws IOException {
        p.load(fis);
        readConfigurationComments();
    }

    /** Closes all the input streams, throwing any Throwables. <b>ONLY TO BE CALLED
     * FROM CleanUp AT SHUTDOWN!!!</b> Don't call me!!
     * @throws IOException Shouldn't matter, as it's only called in CleanUp! But, for completeness, if the FileStreams fail to close, it throws the exception.
     */
    public static void finish() throws IOException {
        fos.close();
        fis.close();
        writeConfiguration();
    }
}

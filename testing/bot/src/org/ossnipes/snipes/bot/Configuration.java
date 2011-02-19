package org.ossnipes.snipes.bot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/** A extension of the {@link Properties} class for the SnipesBot project.<BR/>
 * New features include some new constructors and utility methods for getting of
 * properties as certain types.
 * 
 * @author Jack McCracken
 * @since Snipes 0.01 */
public class Configuration extends Properties
{
	private static final long serialVersionUID = 708148256938967215L;

	public Configuration()
	{
	}

	/** {@inheritDoc} */
	public Configuration(Properties defaults)
	{
		this.defaults = defaults;
	}

	/** Reads the Properties from a filename.
	 * 
	 * @param filename The filename to read from.
	 * @throws IOException If the file is read-protected or could not be found. */
	public Configuration(String filename) throws IOException
	{
		if (filename != null)
		{
			try
			{
				this.load(new FileInputStream(filename));
			} catch (FileNotFoundException e)
			{
				throw new FileNotFoundException(
						"Could not find file specified. File: " + filename);
			} catch (IOException e)
			{
				throw new IOException(
						"Could not read from the file specified. File: "
								+ filename);
			}
			return;
		}
	}

	/** Reads the properties from a {@link Reader} Object.
	 * 
	 * @param readFrom The reader to read from.
	 * @throws IOException */
	public Configuration(Reader readFrom) throws IOException
	{
		this.load(readFrom);
	}

	// /** {@inheritDoc} Also, when using a {@link Configuration} Object, this
	// reads
	// * in the comments. */
	// @Override
	// public synchronized void load(InputStream inStream) throws IOException
	// {
	// // Default behaviour
	// super.load(inStream);
	// // Read in the comments, too.
	// this.readConfigurationComments(inStream);
	// }

	// /** Reads the configuration into the "comments" map.
	// *
	// * @param inStream The {@link InputStream} to read comments from,
	// generally
	// * the one from {@link #load(InputStream)}.
	// * @throws IOException If reading the comments was unsuccessful. It is
	// very
	// * unlikely for this to happen, as the
	// * {@link Properties#load(InputStream)} method probably would
	// * have thrown it. */
	// private void readConfigurationComments(InputStream inStream)
	// throws IOException
	// {
	// // Clear the list of comments, so we can start new.
	// this.comments.clear();
	//
	// // Create something to manage line number counting for us.
	// LineNumberReader lineReader = new LineNumberReader(new BufferedReader(
	// new InputStreamReader(inStream)));
	//
	// // Will hold the text of the current line
	// String currentLine;
	// // Your standard recipe for reading from a BufferedReader.
	// while ((currentLine = lineReader.readLine()) != null)
	// {
	// // If it's a comment
	// if (currentLine.startsWith("#"))
	// {
	// // Put it and it's line number in the comments map
	// this.comments.put(lineReader.getLineNumber(), currentLine);
	// }
	// }
	// }
	/** {@inheritDoc} */
	@Override
	public String getProperty(String key)
	{
		return super.getProperty(key);
	}

	/** {@inheritDoc} */
	@Override
	public String getProperty(String key, String defaultValue)
	{
		return super.getProperty(key, defaultValue);
	}

	/** Gets a property as a boolean value. Anything other than "TRUE" (case
	 * insensitive) is considered false.
	 * 
	 * @param key The key to get and convert to a Boolean.
	 * @return The value of the key, converted to a Boolean Object as specified
	 *         above, or null if the property does not exist. */
	public Boolean getPropertyAsBoolean(String key)
	{
		String prop = this.getProperty(key);

		if (prop == null)
		{
			return null;
		}

		return prop.equalsIgnoreCase("TRUE") ? true : false;
	}

	/** Gets a property as a boolean value. Anything other than "TRUE" (case
	 * insensitive) is considered false.
	 * 
	 * @param key The key to get and convert to a Boolean.
	 * @param defaultValue The value to return if the value does not exist.
	 * @return The value of the key, converted to a Boolean Object as specified
	 *         above, or defaultvalue if the property does not exist. */
	public Boolean getPropertyAsBoolean(String key, boolean defaultValue)
	{
		String prop = this.getProperty(key);

		if (prop == null)
		{
			return defaultValue;
		}

		return prop.equalsIgnoreCase("TRUE") ? true : false;
	}

	/** Gets a property as a Integer value. Anything not a integer returns false.
	 * 
	 * @param key The key to get and convert to a Integer.
	 * @return The value of the key, converted to a Integer Object as specified
	 *         above, or null if the property does not exist or if a error
	 *         occurs while converting the property. */
	public Integer getPropertyAsInteger(String key)
	{
		try
		{
			String s = this.getProperty(key);

			if (s == null)
			{
				return null;
			}

			return Integer.parseInt(s);
		} catch (NumberFormatException e)
		{
			return null;
		}
	}

	/** Gets a property as a boolean value. Anything other than "TRUE" (case
	 * insensitive) is considered false.
	 * 
	 * @param key The key to get and convert to a Boolean.
	 * @param defaultValue The value to return if the value does not exist.
	 * @return The value of the key, converted to a Boolean Object as specified
	 *         above, null if a error occurred while converting the integer, or
	 *         defaultValue if the property did not exist. */
	public Integer getPropertyAsInteger(String key, int defaultValue)
	{
		try
		{
			String s = this.getProperty(key);

			if (s == null)
			{
				return defaultValue;
			}

			return Integer.parseInt(s);
		} catch (NumberFormatException e)
		{
			return null;
		}
	}

	/** Gets the property specified by the key argument and splits it with the
	 * given regular expression. If the value does not exist in the Properties
	 * list, the defaultValue is returned. Trimming can be turned on or off to
	 * remove trailing spaces.
	 * 
	 * @param key The key who's value to split.
	 * @param defaultValue The value to use if the key does not exist in the
	 *            configuration file.
	 * @param regexp The regular expression to split the value by.
	 * @param trim If the result should be trimmed (remove leading and trailing
	 *            spaces).
	 * @return The value of the key split by the specified regexp if it exists,
	 *         and defaultValue otherwise. */
	public String[] getPropertyAsStringArray(String key, String[] defaultValue,
			String regexp, boolean trim)
	{
		String value = this.getProperty(key);

		if (value == null)
		{
			return defaultValue;
		}
		else
		{
			String[] split = value.split(regexp);

			if (trim)
			{
				for (int i = 0; i < split.length; i++)
				{
					split[i] = split[i].trim();
				}
			}
			return split;
		}
	}

	/** Gets the property specified by the key argument and splits it with the
	 * given regular expression. If the value does not exist in the Properties
	 * list, the defaultValue is returned. This method trims spaces by default.
	 * If you don't want to trim spaces, see
	 * {@link #getPropertyAsStringArray(String, String[], String, boolean)}.
	 * 
	 * @param key The key who's value to split.
	 * @param defaultValue The value to use if the key does not exist in the
	 *            configuration file.
	 * @param regexp The regular expression to split the value by.
	 * @return The value of the key split by the specified regexp if it exists,
	 *         and defaultValue otherwise. */
	public String[] getPropertyAsStringArray(String key, String[] defaultValue,
			String regexp)
	{
		return this.getPropertyAsStringArray(key, defaultValue, regexp, true);
	}

	/** Gets the property specified by the key argument and splits it with the
	 * regular expression "," (creating a comma-separated list). If the value
	 * does not exist in the Properties list, the defaultValue is returned. This
	 * method trims spaces by default. If you don't want to trim spaces, see
	 * {@link #getPropertyAsStringArray(String, String[], String, boolean)}.
	 * 
	 * @param key The key who's value to split.
	 * @param defaultValue The value to use if the key does not exist in the
	 *            configuration file.
	 * @return The value of the key split by the regular expression "," if it
	 *         exists, and defaultValue otherwise. */
	public String[] getPropertyAsStringArray(String key, String[] defaultValue)
	{
		return this.getPropertyAsStringArray(key, defaultValue, ",", true);
	}

	/** Gets the property specified by the key argument and splits it with the
	 * regular expression "," (creating a comma-separated list). If the value
	 * does not exist in the Properties list, null is returned. This method
	 * trims spaces by default. If you don't want to trim spaces, see
	 * {@link #getPropertyAsStringArray(String, String[], String, boolean)}.
	 * 
	 * @param key The key who's value to split.
	 * @return The value of the key split by the regular expression "," if it
	 *         exists, and null otherwise. */
	public String[] getPropertyAsStringArray(String key)
	{
		return this.getPropertyAsStringArray(key, new String[]
		{ "" }, ",", true);
	}

	/** {@inheritDoc} */
	@Override
	public void store(OutputStream out, String extraComment) throws IOException
	{
		/* PrintStream bw = new PrintStream(out); // The current line int line =
		 * 1;
		 * 
		 * // Holds all the keys in the properties list. Enumeration<Object>
		 * keys = this.keys();
		 * 
		 * Set<Object> s1 = this.keySet();
		 * 
		 * int totalLines = s1.size() + this.comments.size() + (extraComment !=
		 * null ? 1 : 0) - 1;
		 * 
		 * System.out.println(String.valueOf(totalLines)); if (extraComment !=
		 * null) { bw.println(extraComment); line++; } for (; line <=
		 * totalLines; line++) { if (this.comments.containsKey(line)) {
		 * bw.println(this.comments.get(line)); } else { Object o =
		 * keys.nextElement(); if (!(o instanceof String)) { continue; } String
		 * s = (String) keys.nextElement(); bw.println(s + "=" +
		 * this.getProperty(s)); } } */
		throw new NotImplementedException();
	}
}

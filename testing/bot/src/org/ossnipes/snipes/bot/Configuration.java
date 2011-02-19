package org.ossnipes.snipes.bot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Configuration extends Properties
{
	private static final long serialVersionUID = 708148256938967215L;

	public Configuration()
	{
	}

	public Configuration(Properties defaults)
	{
		this.defaults = defaults;
	}

	public Configuration(String string) throws IOException
	{
		if (string != null)
		{
			try
			{
				this.load(new FileInputStream(string));
			} catch (FileNotFoundException e)
			{
				throw new FileNotFoundException(
						"Could not find file specified. File: " + string);
			} catch (IOException e)
			{
				throw new IOException(
						"Could not read from the file specified. File: "
								+ string);
			}
			return;
		}
	}

	public Configuration(Reader os) throws IOException
	{
		this.load(os);
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

	@Override
	public String getProperty(String key)
	{
		return super.getProperty(key);
	}

	@Override
	public String getProperty(String key, String defaultValue)
	{
		return super.getProperty(key, defaultValue);
	}

	public Boolean getPropertyAsBoolean(String key)
	{
		String prop = this.getProperty(key);

		if (prop == null)
		{
			return null;
		}

		return prop.equalsIgnoreCase("TRUE") ? true : false;
	}

	public Boolean getPropertyAsBoolean(String key, boolean defaultValue)
	{
		String prop = this.getProperty(key);

		if (prop == null)
		{
			return defaultValue;
		}

		return prop.equalsIgnoreCase("TRUE") ? true : false;
	}

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

	public String[] getPropertyAsStringArray(String key, String[] defaultValue,
			String regexp)
	{
		return this.getPropertyAsStringArray(key, defaultValue, regexp, true);
	}

	public String[] getPropertyAsStringArray(String key, String[] defaultValue)
	{
		return this.getPropertyAsStringArray(key, defaultValue, ",", true);
	}

	public String[] getPropertyAsStringArray(String key)
	{
		return this.getPropertyAsStringArray(key, new String[]
		{ "" }, ",", true);
	}

	/** {@inheritDoc} Also, when using a {@link Configuration} Object, this writes
	 * comments. */
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

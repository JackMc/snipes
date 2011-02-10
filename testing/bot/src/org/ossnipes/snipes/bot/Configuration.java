package org.ossnipes.snipes.bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Configuration extends Properties
{
	private static final long serialVersionUID = 708148256938967215L;

	private final Map<Integer, String> comments = new HashMap<Integer, String>();

	private final Map<String, String> temps = new HashMap<String, String>();

	public Configuration(Properties defaults)
	{
		this.defaults = defaults;
	}

	public Configuration()
	{
		this(null);
	}

	/** {@inheritDoc} Also, when using a {@link Configuration} Object, this reads
	 * in the comments. */
	@Override
	public synchronized void load(InputStream inStream) throws IOException
	{
		// Default behaviour
		super.load(inStream);
		// Read in the comments, too.
		this.readConfigurationComments(inStream);
	}

	/** Reads the configuration into the "comments" map.
	 * 
	 * @param inStream The {@link InputStream} to read comments from, generally
	 *            the one from {@link #load(InputStream)}.
	 * @throws IOException If reading the comments was unsuccessful. It is very
	 *             unlikely for this to happen, as the
	 *             {@link Properties#load(InputStream)} method probably would
	 *             have thrown it. */
	private void readConfigurationComments(InputStream inStream)
			throws IOException
	{
		// Clear the list of comments, so we can start new.
		this.comments.clear();

		// Create something to manage line number counting for us.
		LineNumberReader lineReader = new LineNumberReader(new BufferedReader(
				new InputStreamReader(inStream)));

		// Will hold the text of the current line
		String currentLine;
		// Your standard recipe for reading from a BufferedReader.
		while ((currentLine = lineReader.readLine()) != null)
		{
			// If it's a comment
			if (currentLine.startsWith("#"))
			{
				// Put it and it's line number in the comments map
				this.comments.put(lineReader.getLineNumber(), currentLine);
			}
		}
	}

	@Override
	public String getProperty(String key)
	{
		if (this.temps.containsKey(key))
		{
			return this.temps.get(key);
		}
		return super.getProperty(key);
	}

	@Override
	public String getProperty(String key, String defaultValue)
	{
		if (this.temps.containsKey(key))
		{
			return this.temps.get(key);
		}
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
		PrintStream bw = new PrintStream(out);
		// The current line
		int line = 1;

		// Holds all the keys in the properties list.
		Enumeration<Object> keys = this.keys();

		Set<Object> s1 = this.keySet();

		int totalLines = s1.size() + this.comments.size()
				+ (extraComment != null ? 1 : 0) - 1;

		System.out.println(String.valueOf(totalLines));
		if (extraComment != null)
		{
			bw.println(extraComment);
			line++;
		}
		for (; line <= totalLines; line++)
		{
			if (this.comments.containsKey(line))
			{
				bw.println(this.comments.get(line));
			}
			else
			{
				Object o = keys.nextElement();
				if (!(o instanceof String))
				{
					continue;
				}
				String s = (String) keys.nextElement();
				bw.println(s + "=" + this.getProperty(s));
			}
		}
	}

	@Override
	public void store(Writer writer, String comments) throws IOException
	{
		this.store(writer instanceof BufferedWriter ? (BufferedWriter) writer
				: new BufferedWriter(writer), comments);
	}

	/** Sets a temporary property. A temporary property is one that is used for
	 * the duration of Snipes' execution or until someone calls
	 * {@link #clearTemps()}.
	 * 
	 * @param key The key to set temporarily.
	 * @param value The value to set to the key temporarily. */
	public void setTempProperty(String key, String value)
	{
		this.temps.put(key, value);
	}

	/** Clears the temporary properties list. */
	public void clearTemps()
	{
		this.temps.clear();
	}
}

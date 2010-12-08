/*package org.ossnipes.snipes.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.ossnipes.snipes.exceptions.SnipesNotConfiguredException;

public class SerializableConfigReader {
	String path = Constants.CONFNAME;
	InputStream s;
	public SerializableConfigReader(String filepath) throws SnipesNotConfiguredException
	{
		if (filepath != null)
		{
			path = filepath;
		}
		try
		{
			s = new FileInputStream(path);
		} catch (FileNotFoundException e) {throw new SnipesNotConfiguredException("Configuration not found. Please try starting Snipes with the \"--makeconf\" option to configure it.");}
	}
	public SerializableConfigReader(String filepath, InputStream iStream) throws SnipesNotConfiguredException
	{
		if (filepath != null)
		{
			path = filepath;
		}
		
		// Nameless block, only way to make the ifs not overlap.
		{
			if (iStream != null)
			{
				s = iStream;
			}
			else
			{
				try
				{
					s = new FileInputStream(path);
				} catch (FileNotFoundException e) {throw new SnipesNotConfiguredException("Configuration not found. Please try starting Snipes with the \"--makeconf\" option to configure it.");}
			}
		}
	}
	public Configuration readConf() throws IOException
	{
		Configuration conf = null;
		try
		{
			ObjectInputStream sReader = new ObjectInputStream(s);
			Object o = sReader.readObject();
			if (o instanceof Configuration)
				conf = (Configuration)o;
		} catch (Exception e) {throw new IOException();}
		return conf;
	}
}
*/

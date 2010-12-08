/*package org.ossnipes.snipes.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SerializableConfigWriter {
	public static void writeConfiguration(Configuration c, String filepath) throws FileNotFoundException, IOException
	{
		writeConfiguration(c,filepath,null);
	}
	public static void writeConfiguration(Configuration c, String filepath, OutputStream out) throws FileNotFoundException, IOException
	{
		ObjectOutputStream outObj = null;
		if (c == null)
		{
			throw new NullPointerException("writeConfiguration: Configuration c cannot be null.");
		}
		if (filepath == null)
		{
			filepath = Constants.CONFNAME;
		}
		{
			if (out == null)
			{
					outObj = new ObjectOutputStream(new FileOutputStream(filepath));
			}
			else
			{
				try {
					outObj = new ObjectOutputStream(out);
				} catch (IOException e) {}
			}
		}
		outObj.writeObject(c);
	}
}
*/

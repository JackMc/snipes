package org.ossnipes.snipes.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Configuration implements Serializable 
{
	private static final long serialVersionUID = -3030435574041934136L;
	public List<String> plugins = new ArrayList<String>();
	public ArrayList<HashSet<String>> rights;
	public Configuration()
	{
		// Add all the core plugins to the arraylist. This is only called
		//when the object is actually created (in SnipesCreateConf,) so it
		//is a good solution for the problem of arraylists not be initializable
		// to values.
		for (String s : Constants.CORE_PLUGINS)
		{
			plugins.add(s);
		}
	}
}

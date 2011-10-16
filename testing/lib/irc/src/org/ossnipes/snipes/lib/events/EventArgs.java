package org.ossnipes.snipes.lib.events;

/* 
 * 
 * Copyright 2010 Jack McCracken
 * This file is part of The Snipes IRC Framework.
 * 
 * The Snipes IRC Framework is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * The Snipes IRC Framework is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * Although this project was created for use within The Open Source Snipes Project, it is legally 
 * a completely different project. This means that you may distribute it (along with 
 * the source) with your own GPL-compatible projects without having to distribute the actual
 * Implementation project (The Open Source Snipes Project) with it.
 * 
 * Note for other developers of this project: Please include this in any files you create, so that it
 * may be made "legally" a part of the project.
 * 
 * You should have received a copy of the GNU General Public License along with The Snipes IRC Framework. 
 * If not, see http://www.gnu.org/licenses/.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Wrapper class for arguments to Snipes events. 
 * @author jack 
 */
public class EventArgs
{
	private Map<String,Object> _params = null;
	/** Constructs a event arguments object with no keys.
	 */
	public EventArgs(String line)
	{
		_params = new HashMap<String,Object>();
		_params.put("line", line);
	}
	/** Converts the two specified arrays and uses the keys array as the keys for the parameters, and 
	 * uses the values array as the values of the Strings of the keys array with the same index.
	 * @param keys An array of keys, must be the same size as values.
	 * @param values An array of values, must be the same size as keys.
	 * @throws IllegalArgumentException If keys.length != values.length or if keys ==null || values == null.
	 */
	public EventArgs(String line, String[] keys, String[] values)
	{
		if (keys == null || values == null)
			throw new IllegalArgumentException("Keys/values cannot be null.");
		_params = BotUtils.stringObjectArraysToStringObjectMap(keys, values);
		_params.put("line", line);
	}
	/** Creates a EventArgs object with the specified arguments as it's parameters.
	 * @param args The String Object map of the arguments.
	 */
	public EventArgs(String line, Map<String, Object> args)
	{
		if (args == null)
		{
			throw new IllegalArgumentException("args cannot be null.");
		}
		this._params = args;
		_params.put("line", line);
	}
	/** Gets the parameter with the specified key.
	 * @return The value of the parameter. Null if there's no mapping.
	 */
	public Object getParam(String key)
	{
		return _params.get(key);
	}
	/** Gets all the keys in this args set.<BR/>
	 * A common use of this would be: <BR/><BR/>
	 * <code>for (String s : args.getKeySet()) {<BR/>
	 * someList.add(args.getParam(s));<BR/>
	 * }<BR/><BR/>
	 * </code>
	 * Which would add all the values in the args set to someList (or whatever list
	 *  you replaced it with.)
	 * @return A set of all the keys in the args set.
	 */
	public Set<String> getKeySet()
	{
		return _params.keySet();
	}

	/** Retrieves a event parameter as a String, using the {@link Object#toString()} method (so that it is more typesafe than
	 * casting to a String). This is just a convenience method, and is the same as {@link #getParam(String)}.toString().
	 * 
	 * @param key The key to retrieve the value of.
	 * @return The value of the parameter with the specified key.
	 */
	public String getParamAsString(String key)
	{
		Object o = getParam(key);
		if (o == null)
		{
			return null;
		}
		return o.toString();
	}
}

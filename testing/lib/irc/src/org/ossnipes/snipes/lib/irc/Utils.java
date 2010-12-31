package org.ossnipes.snipes.lib.irc;

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

//TODO: Docs.

public class Utils
{
	public static Map<String,Object> stringArraysToStringObjectMap(String[] keys, String[] values)
	{
		
		if (keys.length != values.length)
		{
			throw new IllegalArgumentException("Length of keys array must be the same as length of values array.");
		}
		Map<String,Object> result = new HashMap<String,Object>();
		for (int i = 0; i < keys.length; i++)
		{
			result.put(keys[i], values[i]);
		}
		return result;
	}
}

package org.ossnipes.snipes.spf;

import org.jibble.pircbot.PircBot;

public class SnipesEventParams {
	private static final long serialVersionUID = 6702333471551054447L;
	public SnipesEventParams(PircBot p,String... params)
	{
		_bot = p;
		_params = params;
	}
	public SnipesEventParams(PircBot p)
	{
		_bot = p;
		_params = new String[] {};
	}
	public String[] getParamsArr()
	{
		return _params;
	}
	public PircBot getBot()
	{
		
		return _bot;
	}
	/** Tokenizes the specified index by space
	 * @param paramIndex The index, zero-based (obviously)
	 * @return The tokenized String[] if the index is in range, blank array if not.
	 */
	public String[] getParamEx(int paramIndex)
	{
		if (_params.length < paramIndex + 1)
		{
			return new String [] {};
		}
		else return _params[paramIndex].split(" ");
	}
	String[] _params;
	PircBot _bot;
}

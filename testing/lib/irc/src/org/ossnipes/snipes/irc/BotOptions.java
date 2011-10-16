package org.ossnipes.snipes.irc;

class BotOptions
{
	private BotOptions()
	{
		
	}
	
	static BotOptions getInst()
	{
		return new BotOptions();
	}
	
	boolean isVerbose()
	{
		return _verbose;
	}
	
	boolean isDebugging()
	{
		return _debugging;
	}
	
	void setDebugging(boolean val)
	{
		_debugging = val;
	}
	
	void setVerbose(boolean val)
	{
		_verbose = val;
	}
	
	private boolean _verbose;
	private boolean _debugging;
}

package org.ossnipes.snipes.lib.irc;

public class BotUser 
{
	public BotUser(String nick)
	{
		if (nick == null)
		{
			throw new IllegalArgumentException("Nick cannot be null.");
		}
		_nick = nick;
	}
	
	public String getNick()
	{
		return _nick;
	}
	
	private String _nick;
}

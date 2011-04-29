package org.ossnipes.snipes.lib.irc;

public class BotUser implements BotConstants
{
	public BotUser(String nick)
	{
		if (nick == null)
		{
			throw new IllegalArgumentException("Nick cannot be null.");
		}
		_nick = nick;
	}
	
	public String getNick(boolean withPrefix)
	{
		// Final result
		String nick = _nick;
		if (nick.length() == 0)
		{
			throw new SnipesException("User's nick is empty.");
		}
		
		else if (!withPrefix && BotUtils.arrayContains(IRC_NICKPREFIXES, nick.charAt(0)))
		{
			nick = nick.substring(1);
			System.err.println(nick);
		}
		return nick;
	}
	
	public String getNick()
	{
		return getNick(false);
	}
	
	private String _nick;
}

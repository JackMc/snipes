package org.ossnipes.snipes.lib.events;

import org.ossnipes.snipes.irc.BotConstants;
import org.ossnipes.snipes.irc.SnipesException;
import org.ossnipes.snipes.lib.events.BotUtils;

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
		}
		return nick;
	}
	
	
	// Cannot have a String version of this method. This class does not get the IRCBase instance which manages the channels.
	public boolean isInChannel(Channel channel)
	{
		return channel.isUserInChannel(this);
	}
	
	public String getNick()
	{
		return getNick(false);
	}
	
	private String _nick;
}

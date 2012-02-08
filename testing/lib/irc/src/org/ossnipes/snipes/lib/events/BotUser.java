package org.ossnipes.snipes.lib.events;

import org.ossnipes.snipes.lib.irc.BotConstants;
import org.ossnipes.snipes.lib.irc.SnipesException;

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
		//TODO: More checking for user nicks and other things, this includes a handler for the mode event.
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

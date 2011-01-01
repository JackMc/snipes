package org.ossnipes.snipes.lib.irc;

import java.util.HashMap;
import java.util.Map;

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

/**
 * Class that handles input from the IRC server and sends them off to it's
 * parent.
 * 
 * @author Jack McCracken
 * @since Snipes 0.6
 */

class IRCInputHandler
implements BotConstants,
IRCConstants
{
	IRCInputHandler(IRCBase parent)
	{
		_parent = parent;
	}

	/**
	 * Package-level method to actually handle the lines sent by the IRC server.
	 * 
	 * @param line
	 *            The line that we are to handle. Cannot be null.
	 */
	void handle(String line)
	{
		if (BotOptions.VERBOSE)
		{
			System.out.println(line);
		}
			String[] exSplit = line.split(" ");
			if (exSplit.length == 0)
			{
				return;
			}
				// PING command: we need this or the server'll disconnect us!
				if (exSplit[0].equals("PING"))
				{
					// Key: server = The server we're connected to
					sendEvent(Event.IRC_PING,new EventArgs(new String [] {"server"}, 
							// Substringed because we need to take off the :.
							new String [] {exSplit[1].substring(1)}));
				}
				// PRIVMSG command: If the user sends a PRIVMSG to us or to a channel
				else if (exSplit.length > 4 && exSplit[1].equalsIgnoreCase("PRIVMSG"))
				{
					// Hold the args.
					Map<String,Object> params = new HashMap<String,Object>();
					// Add the sender
					// We don't need to check the length, 
					// it was already checked at the top.
					params.put("from", exSplit[0].split("!")[0].
							// Support for servers which don't 
							// put : in front of the host.
							substring((exSplit[0].startsWith(":") ? 1 : 0)));
					
					// Put the hostname in from-host
					params.put("from-host", exSplit[0].split("@")[1]);
					
					// Add the recipient, this will be getNick()
					// if we receive a PRIVMSG personally.
					params.put("to", exSplit[2]);
					
					/* Start getting the actual message */
					// Variable to hold the message
					String msg;
					// Supporting non-standard servers that don't use : in front of all of them, even 1
					//-word. :\
					if (!exSplit[3].startsWith(":"))
					{
						msg = exSplit[3];
					}
					else
					{
						msg = "";
						for (int i = 3; i < exSplit.length; i++)
						{
							// Is it the first one?
							// Then we need to take away the :.
							if (i == 3)
							{
								msg += exSplit[i].substring(1);
							}
							// We just concat it! :D
							else
							{
								msg += exSplit[i];
							}
						}
					}
					// Stick it into the message variable.
					params.put("message", msg);
					
					sendEvent(Event.IRC_PRIVMSG, new EventArgs(params));
				}
	}
	
	public void sendEvent(Event ev, EventArgs args)
	{
		boolean isInternal = false;
		for (int i = 0; i < INT_EVENTS.length; i++)
		{
			if (INT_EVENTS[i] == ev)
			{
				isInternal = true;
			}
		}
		if (isInternal)
		{
			_parent.handleInternalEvent(ev, args);
		}
		_parent.handleEvent(ev, args);
	}
	
	IRCBase _parent;
}

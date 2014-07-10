package org.ossnipes.snipes.lib.events;

import static org.ossnipes.snipes.lib.events.BotUtils.convertToInt;
import static org.ossnipes.snipes.lib.events.BotUtils.isInteger;
import static org.ossnipes.snipes.lib.events.BotUtils.sendEvent;

import java.util.HashMap;
import java.util.Map;

import org.ossnipes.snipes.lib.irc.BotConstants;
import org.ossnipes.snipes.lib.irc.IRCConstants;
import org.ossnipes.snipes.lib.irc.InputHandler;

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

class EventInputHandler implements BotConstants, IRCConstants, InputHandler
{
	EventInputHandler(IRCBase parent)
	{
		_parent = parent;
	}

	/**
	 * Method to actually handle the lines sent by the IRC server.
	 * 
	 * @param line
	 *            The line that we are to handle. Cannot be null.
	 */
	@Override
	public void handle(String line)
	{
		boolean isResponseCode = false;
		// Is the bot verbose?
		if (_parent.isVerbose())
		{
			System.out.println(line);
		}

		String[] exSplit = line.split(" ");

		// Impossible case
		if (exSplit.length == 0)
		{
			return;
		}

		// When a IRC response is sent to us.
		// This event is checked for before the safeguard because the IRCBase class requires it to test
		// if the nick is already in use.
		if (exSplit.length > 1 && isInteger(exSplit[1]))
		{
			isResponseCode = handleResponseCode(line, exSplit);
		}

		if (!_finishedConnection)
		{
			handleUnfinishedConnection(line, exSplit);
			return;
		}

		// PING command: we need this or the server'll disconnect us!
		if (exSplit[0].equals("PING"))
		{
			handlePing(line, exSplit);
		}

		// PRIVMSG command: If the user sends a PRIVMSG to us or to a channel
		// Example: ":Auv5!~auv5@projectinfinity.net PRIVMSG #Snipes :A IRC PRIVMSG!"
		else if (exSplit.length >= 4 && exSplit[1].equalsIgnoreCase("PRIVMSG"))
		{
			handlePrivMsg(line, exSplit);
		}

		// When the topic is sent to us at join.
		// This is *different* than when the topic is set. That is the next if statement down.
		// Example: ":Equinox.GeekShed.net 332 SnipesBot #Snipes :Article in progress about Snipes :D"
		else if (exSplit.length >= 4 && isInteger(exSplit[1]) &&
				convertToInt(exSplit[1]) == RPL_TOPIC)
		{
			handleOnJoinTopic(line, exSplit);
		}
		// When the topic is changed by (typically) an operator
		// This is *different* than when the topic send to us at join.
		// That is the previous event. If a network offers "services" such as Chanserv with a topic
		// retention feature, then this event may trigger when joining a empty channel.
		// Example: ":Unix!~auv5@projectinfinity.net TOPIC #Snipes :Read this! It's a good document for all who want to use Snipes' plugin API :). http://ossnipes.org/docs/snipes/snipes-article.html | ?? PROFIT"
		else if (exSplit.length >= 4 && exSplit[1].equalsIgnoreCase("TOPIC") && exSplit[2].startsWith("#"))
		{
			handleTopicChange(line, exSplit);
		}
		// When a user joins a channel we are in.
		// Example: ":Unix!rubicon@projectinfinity.net JOIN :#Snipes"
		else if (exSplit.length == 3 && exSplit[1].equalsIgnoreCase("JOIN"))
		{
			handleUserJoined(line, exSplit);
		}

		// When a user or a network service/server sets a mode on a channel we are in.
		// Example: ":ChanServ!services@geekshed.net MODE #Snipes +qo Unix Unix"
		// ?: ":Snipes-RunSetNick MODE Snipes-RunSetNick :+iRx"
		else if (exSplit.length >= 4 && exSplit[1].equalsIgnoreCase("MODE") && (exSplit[3].substring((exSplit[3].startsWith(":") ? 1 : 0)).startsWith("+")
				|| exSplit[3].substring((exSplit[3].startsWith(":") ? 1 : 0)).startsWith("-")))
		{
			handleModeSet(line, exSplit);
		}

		// When a user parts a channel we are in.
		// Example: ":Unix!rubicon@projectinfinity.net PART #Snipes :Ciao."
		// We can't test if there's a message. The RFC says that commands can be sent without a message.
		// This command is similar to the JOIN command :\.
		else if (exSplit.length >= 3 && exSplit[1].equalsIgnoreCase("PART"))
		{
			handleUserParted(line, exSplit);
		}


		// When someone changes their nick in a channel we are in.
		// Example: ":Unix!rubicon@projectinfinity.net NICK Auv5"
		else if (exSplit.length == 3 && exSplit[1].equalsIgnoreCase("NICK"))
		{
			handleUserNickChange(line, exSplit);
		}


		// When a user (possibly us) is kicked from a channel we are in.
		// Example: ":Unix!rubicon@projectinfinity.net KICK #Snipes Auv5[Away] :Event tests ftw :)"
		else if (exSplit.length >= 4 && exSplit[1].equalsIgnoreCase("KICK"))
		{
			handleUserKicked(line, exSplit);
		}

		// When a user sends a NOTICE command to us or a channel we are in.
		// Example: ":Unix!rubicon@projectinfinity.net NOTICE Snipes-RunSetNick :Hello world!"
		else if (exSplit.length >= 4 && exSplit[1].equalsIgnoreCase("NOTICE"))
		{
			handleUserNoticed(line, exSplit);
		}

		// When a user leaves the network in a channel we are in.
		// We cannot produce a "channel" parameter for this command for two reasons:
		// 1. It is not a argument to the command
		// 2. The command is only sent once, regardless of how many channels we have in common with the user.
		// Example: ":Auv5[Away]!~auv5@projectinfinity.net QUIT :Goodbye world!"
		else if (exSplit.length >= 2 && exSplit[1].equalsIgnoreCase("QUIT"))
		{
			handleUserQuit(line, exSplit);
		}

		// If we've tried our best, and we have no idea what it is from all our checks,
		// send a IRC_UNKNOWN event anyways. The user might know something about it.
		// PLEASE KEEP THIS AS THE LAST ELSE IF.
		else if (!isResponseCode)
		{
			handleUnknownEvent(line);
		}
	}

	private void handleUnknownEvent(String line) {
		// Send a unknown event, with the line as the only param.
		sendEvent(new EventArgs(Event.IRC_UNKNOWN, line), _parent);
	}

	private void handleUserQuit(String line, String[] exSplit) {
		// Holds the arguments
		Map<String,Object> params = new HashMap<String,Object>();
		// nick -- The nick of the user joining the channel
		params.put("nick", exSplit[0].split("!")[0].substring(exSplit[0].startsWith(":") ? 1 : 0));
		// host -- The hostname of the user joining
		params.put("host", exSplit[0].split("@")[1]);

		//START getting message (the part message).
		// Check if there is a message
		if (exSplit.length > 2)
		{
			String msg = "";
			// If there is...
			// Stick them together and send it off in the params.
			for (int i = 2; i < exSplit.length; i++)
			{
				msg += (i == 2 ? "" : " ") + exSplit[i];
			}
			params.put("message", msg.substring((msg.startsWith(":") ? 1 : 0)));
		}
		else
		{
			// Map will keep it as null.
		}
		// END getting message


		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_QUIT, line, params),
				_parent);
	}

	private void handleUserNoticed(String line, String[] exSplit) {
		// Hold the args.
		Map<String, Object> params = new HashMap<String, Object>();
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
		// Supporting non-standard servers that don't use : in front of all
		// of them, even 1
		// -word. :\
		if (!exSplit[3].startsWith(":"))
		{
			msg = exSplit[3];
		} else
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
					msg += " " + exSplit[i];
				}
			}
		}

		// Stick it into the message variable.
		params.put("message", msg);

		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_NOTICE, line, params), _parent);
	}

	private void handleUserKicked(String line, String[] exSplit) {
		Map<String,Object> params = new HashMap<String,Object>();
		// kicker -- The person who sent the /KICK command.
		params.put("kicker", exSplit[0].split("!")[0].substring(exSplit[0].startsWith(":") ? 1 : 0));
		// I'm a little iffy on this param. It might need
		params.put("kicker-host", exSplit[0].split("@")[1]);
		params.put("channel", exSplit[2]);
		params.put("kicked", exSplit[3]);
		// We need to check if there was a message.
		//START getting message (the part message).
		if (exSplit.length > 4)
		{
			String msg = "";
			// If there is...
			// Stick them together and send it off in the params.
			for (int i = 4; i < exSplit.length; i++)
			{
				msg += (i == 4 ? "" : " ") + exSplit[i];
			}
			params.put("message", msg.substring((msg.startsWith(":") ? 1 : 0)));
		}
		else
		{
			// Map will keep it as null.
		}
		// END getting mode-param

		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_KICK, line, params), _parent);
	}

	private void handleUserNickChange(String line, String[] exSplit) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("nick-old", exSplit[0].split("!")[0].substring((exSplit[0].startsWith(":") ? 1 : 0)));
		params.put("nick-new", exSplit[2].substring(exSplit[2].startsWith(":") ? 1 : 0));
		params.put("host", exSplit[0].split("@")[1]);

		sendEvent(new EventArgs(Event.IRC_NICK_CHANGE, line, params), _parent);
	}

	private void handleUserParted(String line, String[] exSplit) {
		// Holds the arguments
		Map<String,Object> params = new HashMap<String,Object>();
		// nick -- The nick of the user parting the channel
		params.put("nick", exSplit[0].split("!")[0].substring(exSplit[0].startsWith(":") ? 1 : 0));
		// host -- The hostname of the user parting
		params.put("host", exSplit[0].split("@")[1]);
		// channel -- The channel being left.
		params.put("channel", exSplit[2]);

		//START getting message (the part message).
		// Check if there is a message
		if (exSplit.length > 3)
		{
			String msg = "";
			// If there is...
			// Stick them together and send it off in the params.
			for (int i = 3; i < exSplit.length; i++)
			{
				msg += (i == 3 ? "" : " ") + exSplit[i];
			}
			params.put("message", msg.substring((msg.startsWith(":") ? 1 : 0)));
		}
		else
		{
			// Map will keep it as null.
		}
		// END getting message


		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_PART, line, params),
				_parent);
	}

	private void handleModeSet(String line, String[] exSplit) {
		Map<String,Object> params = new HashMap<String,Object>();
		// channel -- The channel the mode is being set on
		params.put("channel", exSplit[2]);
		// TODO: Should really make this more clear. It works, but it seems a bit hard to understand. Determine if it is worth the extra time checking if it is a server.
		// It works because when splitting a String, you always get at least a 1-length array back. This means that your String does not have to contain a ! for this to work,
		// And it just returns the entire String in the first element.
		// setter -- The nick of the nick setting the mode, or the address of the server if it is a server setting it.
		params.put("setter", exSplit[0].split("!")[0].substring((exSplit[0].startsWith(":") ? 1 : 0)));

		// START getting setter-host (the hostname of the user setting the mode, or the address of the server if it's a server setting it)
		String[] atSplit = exSplit[0].split("@");

		if (atSplit.length == 1)
		{
			// It's a server. Just give them the same as setter.
			params.put("setter-host", params.get("setter"));
		}
		else if (atSplit.length > 1)
		{
			// Normal host-splitting behavior.
			params.put("setter-host", atSplit[1]);
		}
		else
		{
			System.err.println("Reached imposable case. Please check which dimension you are in. A split of a String always returns at least a 1-length String array. " +
			"If this is not true on your implementation of the JDK, please tell me :3.");
			System.exit(2);
		}
		// END getting setter-host

		params.put("mode", exSplit[3]);

		//START getting mode-params (the parameters after the mode, e.g. the part after +qo in ":ChanServ!services@geekshed.net MODE #Snipes +qo Unix Unix")
		// Check if there are any params to the mode.
		if (exSplit.length > 4)
		{
			String msg = "";
			// If there are...
			// Stick them together and send it off in the params.
			for (int i = 4; i < exSplit.length; i++)
			{
				msg += (i == 4 ? "" : " ") + exSplit[i];
			}
			params.put("mode-params", msg);
		}
		else
		{
			// Map will keep it as null.
		}
		// END getting mode-params

		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_MODE, line, params), _parent);
	}

	private void handleUserJoined(String line, String[] exSplit) {
		// More support for protocol defiance
		String channel = exSplit[2].substring((exSplit[2].startsWith(":") ? 1 : 0));
		// Holds the arguments
		Map<String,Object> params = new HashMap<String,Object>();
		// nick -- The nick of the user joining the channel
		params.put("nick", exSplit[0].split("!")[0].substring(exSplit[0].startsWith(":") ? 1 : 0));
		// host -- The hostname of the user joining
		params.put("host", exSplit[0].split("@")[1]);
		// channel -- The channel being joined.
		// Oh, look at that. We already have a variable for that.
		params.put("channel", channel);
		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_JOIN, line, params),
				_parent);
	}

	private void handleTopicChange(String line, String[] exSplit) {
		Map<String, Object> params = new HashMap<String, Object>();
		// Stick the setter's nick in there.
		params.put("setter", exSplit[0].split("!")[0]);
		// The host of the setter
		params.put("setter-host", exSplit[0].split("@")[1]);
		// The channel the topic was set on
		params.put("channel", exSplit[2]);

		String msg;

		if (!exSplit[3].startsWith(":"))
		{
			msg = exSplit[3];
		} else
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
					msg += " " + exSplit[i];
				}
			}
		}
		// Actually put the parameter! :).
		params.put("topic", msg);

		// Fire off the event! :)
		sendEvent(new EventArgs(Event.IRC_TOPIC, line, params),
				_parent);
	}

	private void handleOnJoinTopic(String line, String[] exSplit) {
		// Holds the parameters
		Map<String, Object> params = new HashMap<String, Object>();
		// Server: The server sending the notice to us.
		params.put("server", exSplit[0].substring(1));
		// Channel: The channel we're being notified about.
		params.put("channel", exSplit[3]);
		// Topic: The actual text of the topic.
		// We need to fiddle with this, taking off the : (unless we're on a nasty
		// protocol-denier :().
		// Also, we need to stick it together, I forgot about that.

		// Will hold the final topic string.
		String msg;

		if (!exSplit[4].startsWith(":"))
		{
			msg = exSplit[4];
		} else
		{
			msg = "";
			for (int i = 4; i < exSplit.length; i++)
			{
				// Is it the first one?
				// Then we need to take away the :.
				if (i == 4)
				{
					msg += exSplit[i].substring(1);
				}
				// We just concat it! :D
				else
				{
					msg += " " + exSplit[i];
				}
			}
		}
		// Actually put the parameter! :).
		params.put("topic", msg);

		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_JOIN_TOPIC, line, params), _parent);
	}

	private void handlePrivMsg(String line, String[] exSplit) {
		// Hold the args.
		Map<String, Object> params = new HashMap<String, Object>();
		// Add the sender
		// We don't need to check the length,
		// it was already checked at the top.
		params.put("from", exSplit[0].split("!")[0].
				// Support for servers which don't
				// put : in front of the host.
				substring((exSplit[0].startsWith(":") ? 1 : 0)));

		// Put the hostname in from-host
		params.put("from-host", exSplit[0].split("@")[1]);
		
		String chan = exSplit[2];
		
		for (char pre : IRC_CHANPREFIXES)
		{
			if (chan.length() == 0)
			{
				break;
			}
			if (pre == chan.charAt(0))
			{
				params.put("channel", chan);
			}
		}
		
		// Add the recipient, this will be getNick()
		// if we receive a PRIVMSG personally.
		// This is maintained for compatibility.
		params.put("to", exSplit[2]);
		
		// Add who replies to this message should be generally sent to.
		params.put("sendto", params.get("channel") != null ? params.get("channel") : params.get("from"));

		/* Start getting the actual message */
		// Variable to hold the message
		String msg;
		// Supporting non-standard servers that don't use : in front of all
		// of them, even 1
		// -word. :\
		if (!exSplit[3].startsWith(":"))
		{
			msg = exSplit[3];
		} else
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
					msg += " " + exSplit[i];
				}
			}
		}

		// Stick it into the message variable.
		params.put("message", msg);

		// Fire off the event.
		sendEvent(new EventArgs(Event.IRC_PRIVMSG,line, params), _parent);
	}

	private void handlePing(String line, String[] exSplit) {
		// Key: server = The server we're connected to
		sendEvent(new EventArgs(Event.IRC_PING, 
				line, new String[]{"server"},
				// Substringed because we need to take off the :.
				new String[]{exSplit[1].substring(1)}), _parent);
	}

	private boolean handleResponseCode(String line, String[] exSplit) {
		boolean isResponseCode;
		isResponseCode = true;
		Map<String,Object> params = new HashMap<String,Object>();
		int code = convertToInt(exSplit[1]);
		// The response code
		params.put("code", code);

		// We need to stick it together.
		String msg;
		msg = "";
		for (int i = 3; i < exSplit.length; i++)
		{
			// Is it the first one?
			// Then we may need to take away the :.
			if (i == 3)
			{
				msg += exSplit[i].substring((exSplit[i].startsWith(":") ? 1 : 0));
			}
			// We just concat it! :D
			else
			{
				msg += " " + exSplit[i];
			}
		}

		params.put("text", msg);

		params.put("server", exSplit[0].substring(1));

		// We won't fire off the IRC_RESPONSE_CODE now, because
		// we want the IRC_NICKINUSE internal handler to be able to terminate
		// before the other handlers get the IRC_RESPONSE_CODE.
		//BEGIN EXTRA CHECKS FOR ERR_NICKNAMEINUSE
		if (code == ERR_NICKNAMEINUSE)
		{
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("fatal", !_finishedConnection);
			sendEvent(new EventArgs(Event.IRC_NICKINUSE, line, args), _parent);
		}
		//END EXTRA CHECKS FOR ERR_NICKNAMEINUSE
		// Fire off the IRC_RESPONSE_CODE
		sendEvent(new EventArgs(Event.IRC_RESPONSE_CODE, line, params), _parent);
		return isResponseCode;
	}

	private void handleUnfinishedConnection(String line, String[] exSplit) {
		// If it's more than or equal to four, we can
		// continue.
		if (line.length() >= 4)
		{
			// Check for a response code guaranteed to be sent by the server
			// Before the MOTD. We don't want to use the end of MOTD response
			// for this, because that would mean that this wouldn't work on
			// servers with no MOTD.
			if (isInteger(exSplit[1]) && convertToInt(exSplit[1])
					== RPL_LUSERME)
			{
				_finishedConnection = true;
			}
		}
		return;
	}



	IRCBase _parent;
	// Solution to the problem of the VERSION message being a PRIVMSG :\.
	private boolean _finishedConnection = false;
}
package org.ossnipes.snipes.lib.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.ossnipes.snipes.lib.irc.IRCConstants;
import org.ossnipes.snipes.lib.irc.SnipesException;

public class Channel implements IRCEventListener, IRCConstants
{
	//TODO: Nick change support (shouldn't be that hard to do.) There may be complications to do with shared BotUser Objects. Maybe they could hold their previous nicks, and we could check that too.
	//TODO: We also need to track as users part/join.
	Channel(String name)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name cannot be null.");
		}
		_name = name;
	}
	
	
	@Override
	public Event[] getRegisteredEvents() 
	{
		return new Event [] {Event.IRC_RESPONSE_CODE, Event.IRC_JOIN_TOPIC, Event.IRC_TOPIC, Event.IRC_JOIN, Event.IRC_PART};
	}

	@Override
	public void handleEvent(Event ev, EventArgs args) 
	{
		
		if (ev == Event.IRC_RESPONSE_CODE && !_doneNamesRecv)
		{
			int code = ((Integer)args.getParam("code"));
			String[] split = args.getParamAsString("text").split(" ");
			if (split.length == 0)
			{
				// Impossible
				throw new InternalError();
			}
			
			if (code == RPL_NAMREPLY)
			{
				if (!split[1].equalsIgnoreCase(getName()))
				{
					return;
				}
				StringTokenizer strtok = new StringTokenizer(args.getParamAsString("text"));
				
				boolean prevStartRecording = false;
				boolean startRecording = false;
				while (strtok.hasMoreTokens())
				{
					String next = strtok.nextToken();
					prevStartRecording = startRecording;
					startRecording = startRecording || next.startsWith(":");
					
					if (!startRecording)
					{
						continue;
					}
					else
					{
						String userNickWPrefix = next;
						if (!prevStartRecording)
						{
							userNickWPrefix = userNickWPrefix.substring(1);
						}
						addUser(userNickWPrefix);
					}
				}
			}
			else if (code == RPL_ENDOFNAMES)
			{
				if (!split[0].equalsIgnoreCase(getName()))
				{
					return;
				}
				// We're done getting the names.
				_doneNamesRecv = true;
			}
		}
		else if (ev == Event.IRC_JOIN_TOPIC || ev == Event.IRC_TOPIC)
		{
			if (args.getParamAsString("channel").equalsIgnoreCase(getName()))
			{
				_topic = args.getParamAsString("topic");
			}
		}
		else if (ev == Event.IRC_JOIN)
		{
			String nick = args.getParamAsString("nick");
			if (args.getParamAsString("channel").equalsIgnoreCase(getName()))
			{
				addUser(nick);
			}
		}
		else if (ev == Event.IRC_PART)
		{
			String nick = args.getParamAsString("nick");
			if (args.getParamAsString("channel").equalsIgnoreCase(getName()))
			{
				BotUser user = null;
				for (BotUser u : _currentUsers)
				{
					if (u.getNick().equalsIgnoreCase(nick))
					{
						user = u;
						break;
					}
				}
				if (user == null)
				{
					throw new SnipesException(nick + " was not found in the list of users, yet they parted channel " + getName() + ".");
				}
				else
				{
					_currentUsers.remove(user);
				}
			}
		}
		else if (ev == Event.IRC_RESPONSE_CODE)
		{
			return;
		}
		else
		{
			System.err.println("Snipes IRC API Channel Tracking: Unknown event was sent to us: " + ev + ".");
		}
	}

	private void addUser(String userNickWPrefix) 
	{
		int i = 0;
		while (i < _userPool.size())
		{
			BotUser bu = _userPool.get(i);
			
			if (bu.getNick().equalsIgnoreCase(userNickWPrefix))
			{
				_currentUsers.add(bu);
				break;
			}
			
			i++;
		}
		
		// If it looped without any matches.
		if (i == _userPool.size())
		{
			BotUser newU = new BotUser(userNickWPrefix);
			_currentUsers.add(newU);
			_userPool.add(newU);
		}
	}
	
	public BotUser[] getUsers()
	{
		return _currentUsers.toArray(new BotUser[_currentUsers.size()]);
	}
	
	public String getName()
	{
		return _name;
	}
	
	public boolean isUserInChannel(BotUser bu)
	{
		return isUserInChannel(bu.getNick());
	}
	
	public boolean isUserInChannel(String nick)
	{
		for (BotUser bu : _currentUsers)
		{
			if (bu.getNick().equals(nick))
			{
				return true;
			}
		}
		return false;
	}
	
	public BotUser getUserForName(String name)
	{
		for (BotUser bu : _currentUsers)
		{
			if (bu.getNick().equalsIgnoreCase(name))
			{
				return bu;
			}
		}
		return null;
	}
	
	public String getTopic()
	{
		return _topic;
	}
	
	private String _name;
	private String _topic;
	private List<BotUser> _currentUsers = new ArrayList<BotUser>();
	private boolean _doneNamesRecv = false;
	private static List<BotUser> _userPool = Collections.synchronizedList(new ArrayList<BotUser>());
}

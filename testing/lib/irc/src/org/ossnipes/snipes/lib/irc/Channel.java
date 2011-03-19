package org.ossnipes.snipes.lib.irc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

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
		return new Event [] {Event.IRC_RESPONSE_CODE, Event.IRC_JOIN_TOPIC, Event.IRC_TOPIC};
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
				if (!split[1].equalsIgnoreCase(_name))
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
				if (!split[0].equalsIgnoreCase(_name))
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

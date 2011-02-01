package org.ossnipes.snipes.lib.irc;

import java.util.List;

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
 * If not, see http:www.gnu.org/licenses/.
 */

/** This class represents a user on IRC
 * @author Jack McCracken
 * @since Snipes 0.6
 */
public class IRCUser
implements IRCConstants,
IRCEventListener
{
	boolean lookingForHost = false;
	IRCUser(final IRCBase parent, final String nick)
	{
		_parent = parent;
		_nick = nick;
                _parent.addEventListener(this);
                 startGet();
	}
	
    public void handleEvent(Event ev, EventArgs args)
    {
        switch (ev)
        {
        case IRC_RESPONSE_CODE:
        {
        	if (((Integer)args.getParam("code")) == RPL_NAMREPLY)
        	{
        		System.out.println("Got a names reply! :D");
        	}
        }
        }
    }
    
	/** Gets this user Object's hostname. */
	private void startGet()
	{
		// Have we gotten this user's host before?
		if (_hostname == null)
		{
			// Populate the hostname variable.
			beginGettingUserHost();
		}
	}
	
	private void beginGettingUserHost()
	{
		lookingForHost = true;
                 _parent.who(_nick);
	}
    
    private void populateUserHost(String host)
    {
        if (host != null)
        {
        	_hostname = host;
        }
    }

	private void setHostname(String hostname)
	{
        if (_hostname == null)
        {
            throw new IllegalArgumentException("hostname cannot be null.");
        }
        _hostname = hostname;
	}
	
	public String getNick()
	{
		return _nick;
	}
	
    
    public Event[] register() {return new Event[] {Event.IRC_RESPONSE_CODE};}
	// Class-scope variables
	String _hostname;
	String _nick;
	IRCBase _parent;
        List<String> _channels;
}

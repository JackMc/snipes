package org.ossnipes.snipes.lib.irc;

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

/**
 * Interface for all bot constants (bot constants are constants that are not IRC
 * response codes)
 * 
 * @since Snipes 0.6
 * @author Jack McCracken
 */
public interface BotConstants
{
	/** The float-represented version of the Snipes IRC library */
	static final float SNIRC_VERSION = 1.01f;
	/** The String representation of {@link #SNIRC_VERSION} */
	static final String SNIRC_VERSION_STRING = Float.toString(SNIRC_VERSION);
	/** The default port for IRC servers */
	static final int IRC_DEFAULT_PORT = 6667;
	/** The IRC server timeout in milliseconds */
	static final int IRC_TIMEOUT = 120 * 1000;
	static final Character[] IRC_CHANPREFIXES = {'#', '&', '+'};
	static final Character[] IRC_NICKPREFIXES = {'~', '&', '@', '%'};
	/**
	 * The default username (before the @ and after the ! in the hostname) if it
	 * has not been set.
	 */
	static final String DEFAULT_USER = "SnipesBot";
	/**
	 * The default nick (you know what a nick is :P) if it has not been set
	 */
	static final String DEFAULT_NICK = "Snipes-RunSetNick";
	/**
	 * The default realname for the bot if it has not been set
	 */
	static final String DEFAULT_REALNAME = "Snipes IRC bot, version " + SNIRC_VERSION_STRING;

	/** The set of events that need handling by the bot framework itself, along with possibly 
	 * the user.
	 */
	static final Event[] INTERNAL_EVENTS = { Event.IRC_PING, Event.IRC_JOIN, Event.IRC_NICKINUSE };
}

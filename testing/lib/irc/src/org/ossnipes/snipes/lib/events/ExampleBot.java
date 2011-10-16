package org.ossnipes.snipes.lib.events;

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

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;

public class ExampleBot extends IRCBase
{
	public static void main(String args[])
	{
		new ExampleBot().run();
	}
	
	public void run()
	{
		try
		{
			setRealname("Snipes IRC Framework example bot.");
			setVerbose(true);
			setNick("Snipes-TestBot");
			connect("irc.geekshed.net");
			join("#Snipes");
			join("#Snipes-Testing");
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public synchronized void handleEvent(Event ev, EventArgs args)
	{
		Set<String> keys = args.getKeySet();
		StringBuilder stringBuilder = new StringBuilder("Got an event: " + ev.toString() + " ");
		int counter = 0;
		for (String s : keys)
		{
			stringBuilder.append((counter != 0 ? " " : "") + s + "=\"" + args.getParam(s) + "\"");
			counter ++;
		}

		System.err.println(stringBuilder.toString());
		if (ev == Event.IRC_PRIVMSG)
		{
			String msg = (String)args.getParam("message");
			String[] msgSplit = msg.split(" ");
			if (msgSplit.length == 2 && msgSplit[0].equalsIgnoreCase("!nick"))
			{
				setNick(msgSplit[1]);
			}
		}
	}
}

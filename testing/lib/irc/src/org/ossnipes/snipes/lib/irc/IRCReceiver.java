package org.ossnipes.snipes.lib.irc;

import java.io.IOException;

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
 * Encapsulates the receiving of data from the IRC server.
 * 
 * @since Snipes 0.6
 * @author Jack McCracken
 */

class IRCReceiver implements Runnable
{
	// Lock for usage of the handler.
	private Object lock = new Object();
	
	/**
	 * Instantiates a object of the class.
	 * 
	 * @param manager
	 *            The socket manager that this thread should be watching for
	 *            messages
	 * @param initialHandler
	 *            The "parent" IRCBase of this object. In other words: The
	 *            object it will notify if it gets a message.
	 */
	IRCReceiver(IRCSocketManager manager, InputHandler initialHandler)
	{
		_manager = manager;
		_handler = initialHandler;
	}

	public void run()
	{
		try
		{
			// Loop until the bot disconnects from the server.
			while (isConnected())
			{
				String s = _manager.recvRaw();
				if (s != null)
				{
					synchronized (lock) {
						_handler.handle(s);	
					}
				}
				else
				{
					System.out.println("Disconnected from server.");
					break;
				}
			}
		} catch (IOException e)
		{/*
		 * :( we've Disconnected, or we've been disconnected. Just return.
		 */
			System.err.println("Disconnected unexpectedly: " + e.getMessage());
		}
	}
	
	public void setHandler(InputHandler newIn)
	{
		synchronized (lock) 
		{
			_handler = newIn;
		}
	}

	public boolean isConnected()
	{
		return _manager.isConnected();
	}

	private IRCSocketManager _manager;
	private InputHandler _handler;

}

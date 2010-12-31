package org.ossnipes.snipes.lib.irc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

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
 * The default socket factory for all IRC operations within the bot. The
 * reasoning for using a factory and not a straight new Socket(host,port) is
 * because with this approach, we can have certain options set on by default
 * with the #createSocket method (also to move more towards the "singleton"
 * method).
 * 
 * If you wish to use SSL when connecting to a server, try
 * {@link SnipesSSLSocketFactory}
 * 
 * @since Snipes 0.6 Jack McCracken (<a
 *        href="http://ossnipes.org/">http://ossnipes.org</a>)
 * @see SnipesSSLSocketFactory
 */

public class SnipesSocketFactory extends SocketFactory implements BotConstants
{

	private static final SocketFactory def = new SnipesSocketFactory();

	// We are the only people able to instantiate this factory
	protected SnipesSocketFactory()
	{
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException
	{
		Socket s = new Socket(host, port);
		s.setSoTimeout(IRC_TIMEOUT);
		return s;
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException
	{
		Socket s = new Socket(host.getHostName(), port);
		s.setSoTimeout(IRC_TIMEOUT);
		return s;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost,
			int localPort) throws IOException, UnknownHostException
	{
		Socket s = new Socket(host, port, localHost, localPort);
		s.setSoTimeout(IRC_TIMEOUT);
		return s;
	}

	@Override
	public Socket createSocket(InetAddress address, int port,
			InetAddress localAddress, int localPort) throws IOException
	{
		Socket s = new Socket(address.getHostName(), port, localAddress,
				localPort);
		s.setSoTimeout(IRC_TIMEOUT);
		return s;
	}

	public static SocketFactory getDefault()
	{
		return def;
	}
}

package org.ossnipes.snipes.lib.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/* 
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
 * Manages the Socket to a IRC server. This class is meant to make sending and
 * receiving to/from a IRC server easier. It also allows the implementation to
 * be changed much easier.
 * 
 * @since Snipes 0.6
 * @author Jack McCracken
 */

class IRCSocketManager
{

	/**
	 * Creates a IRC Socket Manager object.
	 * 
	 * @param rawSocket
	 *            The Socket Object to manage. This Socket must be open or a
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws IOException
	 *             If there was a error (besides the stream being closed) while
	 *             opening the streams.
	 */
	IRCSocketManager(Socket rawSocket) throws IOException
	{
		// If the provided Socket is null, throw an IllegalArgumentException
		if (rawSocket == null)
		{
			throw new IllegalArgumentException("rawSocket cannot be null.");
		}

		if (!rawSocket.isConnected())
		{
			throw new IOException("rawSocket is not connected to a server.");
		}

		_rawSocket = rawSocket;
		_reader = new BufferedReader(new InputStreamReader(_rawSocket
				.getInputStream()));
		_writer = new PrintStream(_rawSocket.getOutputStream());
	}

	/**
	 * Sends a raw line to the IRC server.
	 * 
	 * @param line
	 *            The line to send to the server.
	 */
	public void sendRaw(String line)
	{
		if (BotOptions.VERBOSE)
		{
			System.out.println("US: " + line);
		}
		_writer.println(line);
	}

	/**
	 * Reads a line of text from the server.
	 * 
	 * @return The String from the server, null if a Exception occurs.
	 * @throws IOException
	 *             If we've been disconnected, or something else happens.
	 */
	public String recvRaw() throws IOException
	{
		try
		{
			return _reader.readLine();
		} catch (IOException e)
		{
			throw new IOException("We have been killed!");
		}
	}

	public boolean isConnected()
	{
		return !_rawSocket.isClosed();
	}

	// Class-scope variables
	/** The raw socket we're managing */
	private Socket _rawSocket;
	/**
	 * The Reader that reads from _rawSocket, initialised at Object construction
	 */
	private BufferedReader _reader;
	/** The Writer that writes to _rawSocket, initialised at Object construction */
	private PrintStream _writer;
}

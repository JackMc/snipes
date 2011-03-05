package org.ossnipes.snipes.console;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Console 
{
	public static int DEFAULT_PORT = 9001;
	public Console(String[] args)
	{
		for (String s : args)
		{
			if (s.equalsIgnoreCase("--debug") || s.equalsIgnoreCase("-d"))
			{
				DEBUG = true;
			}
		}
		
		debug("Finished parsing command line arguments.");
		_cm = new ConsoleManager(new Scanner(System.in));
		
		String server = _cm.getConsoleLine("Please enter the bot's IP/host: ");
		if (server == null || server.isEmpty())
		{
			System.err.println("Unable to get server name from STDIN.");
			System.exit(1);
		}
		String serverHost = null;
		int port = DEFAULT_PORT;
		
		String[] serverSplit = server.split(":");
		
		if (serverSplit.length > 2 || serverSplit.length == 0)
		{
			System.err.println("Ill formatted server name.");
			System.exit(2);
		}
		else if (serverSplit.length == 1)
		{
			serverHost = serverSplit[0];
		}
		else if (serverSplit.length == 2)
		{
			serverHost = serverSplit[0];
			try
			{
				port = Integer.parseInt(serverSplit[1]);
			} catch (NumberFormatException e) 
			{
				System.err.println("Ill formatted port.");
				System.exit(3);
			}
		}
		
		try {
			_sm = new SocketManager(new Socket(serverHost, port));
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + serverHost);
			System.exit(4);
		} catch (IOException e) {
			System.err.println("Unknown IOException while connecting to NoIRCMan server. Message: " + e.getMessage() + ".");
			System.exit(5);
		}
		
		String s = null;
		try {
			debug("Calling recv() to wait for HELLO packet.");
			s = _sm.recv();
			debug("Line received. Line: " + s);
		} catch (IOException e) {
			debug("Could not receive line");
			System.err.println("Unable to receive line from NoIRCMan server.");
			System.exit(6);
		}
		
		if (s == null)
		{
			debug("Null line received from server.");
			System.err.println("Unable to receive line from NoIRCMan server.");
			System.exit(6);
		}
		
		if (s.equalsIgnoreCase("HELLO"))
		{
			String user = _cm.getConsoleLine("Please enter your NoIRCMan username: ");
			String pass = _cm.getConsoleLine("Please enter your NoIRCMan password [default blank]: ");
			
			if (user == null)
			{
				System.err.println("Unable to get username from STDIN.");
				System.exit(1);
			}
			
			if (pass == null)
			{
				System.err.println("Unable to get password from STDIN.");
				System.exit(1);
			}
			
			if (pass.isEmpty())
			{
				pass = "-";
			}
			_sm.sendln("HELLO");
			_sm.sendln(user);
			_sm.sendln(pass);
			try {
				String response = _sm.recv();
				if (response.equalsIgnoreCase("GOODBYE"))
				{
					System.err.println("Incorrect username or password.");
					System.exit(7);
				}
				else if (response.equalsIgnoreCase("WELCOME"))
				{
					System.out.println("Username and password were correct. You may now being typing commands.");
				}
				else
				{
					System.err.println("Incorrect protocol response.");
					System.exit(8);
				}
					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("Did not receive expected HELLO command from NoIRCMan server. Received command: " + s);
		}
		
		startConsoleLoop();
	}
	
	private void startConsoleLoop() {
		boolean running = true;
		while (running)
		{
			String line = _cm.getConsoleLine(">>> ");
			String[] lineSplit = line.split(" ");
			try
			{
				if (line.equalsIgnoreCase("hw"))
				{
					_sm.sendln("SAYHELLO");
					processCommand(_sm.recv());
				}
				else if (lineSplit[0].equalsIgnoreCase("PROTORAW"))
				{
					if (lineSplit.length < 2)
					{
						System.err.println("Incorrect syntax for PROTORAW command. Use like: PROTORAW <raw protocol command>");
					}
					if (lineSplit[1].isEmpty())
					{
						System.err.println("Cannot send an empty line.");
					}
					String s = line.substring(line.indexOf(' ') + 1);
					debug("Sending raw command " + s);
					_sm.sendln(s);
					// Account for network lag.
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// Oh well...
					}
				}
				else if (lineSplit[0].equalsIgnoreCase("recv"))
				{
					if (lineSplit.length > 2)
					{
						System.err.println("Incorrect syntax for recv command. Use like: recv [optional number of lines]");
					}
					else if (lineSplit.length == 1)
					{
						System.out.println(_sm.recv());
					}
					else
					{
						try
						{
							int num = Integer.parseInt(lineSplit[1]);
							if (num > 10)
							{
								String s;
								while ((s = _cm.getConsoleLine("Are you sure you want to receive " + num + " lines? This will block the program until " + num + 
										" lines are received [Y/N]: ")).equalsIgnoreCase("Y") || !s.equalsIgnoreCase("N"));
								if (s.equalsIgnoreCase("N"))
								{
									return;
								}
							}
							int i = 0;
							while (i <= num)
							{
								System.out.println(_sm.recv());
							}
						} catch (NumberFormatException e) 
						{
							System.err.println(lineSplit[1] + " is not a valid integer.");
						}
						
					}
				}
			} catch (IOException e) 
			{
				System.err.println("Error while receiving line from the server.");
			}
		}
	}

	private void processCommand(String recv) throws IOException 
	{
		if (recv == null)
		{
			System.err.println("Server closed connection unexpectedly.");
		}
		else if (recv.equalsIgnoreCase("PRNT"))
		{
			System.out.println(_sm.recv());
		}
	}

	public static void main(String args[])
	{
		new Console(args);
	}
	
	public void debug(String line)
	{
		if (DEBUG)
		{
			_l.log(Level.INFO, line);
		}
	}
	
	private Logger _l = Logger.getLogger(this.getClass().getCanonicalName());
	private ConsoleManager _cm;
	private SocketManager _sm;
	private boolean DEBUG = false;
}

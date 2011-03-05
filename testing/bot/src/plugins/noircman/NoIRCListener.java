package plugins.noircman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCEventListener;

public class NoIRCListener extends Thread implements NoIRCConstants,
		IRCEventListener
{
	public NoIRCListener(NoIRCModule noIRCModule)
	{
		this._parent = noIRCModule;
		this.start();
	}

	@Override
	public void run()
	{
		this._parent.addEventListener(this);
		Thread.currentThread().setName("NoIRC-Listener");
		Integer port = this._parent.getConfiguration().getPropertyAsInteger(
				"noircport", DEFAULT_PORT);
		if (port == null)
		{
			System.err
					.println("The value of the \"noircport\" property is not a valid integer. Using default "
							+ DEFAULT_PORT);
			port = DEFAULT_PORT;
		}
		try
		{
			this._ss = new ServerSocket(port);
		} catch (IOException e)
		{
			System.err.println("Could not bind to port " + port + ". Error: "
					+ e.getMessage() + ".");
			return;
		}

		while (this._parent.isConnected())
		{
			try
			{
				Socket s = this._ss.accept();
				ConnectionManager cm = new ConnectionManager(
						new ConsoleConnection(s), this._parent);
				this.registerManager(cm);
			} catch (IOException e)
			{
				System.err.println("Could not accept connections on port "
						+ port + ".");
				break;
			}
		}

		for (ConnectionManager cm : this._mans)
		{
			if (cm.isAlive())
			{
				cm.requestStop();
			}
		}
	}

	private void registerManager(ConnectionManager cm)
	{
		this._mans.add(cm);
	}

	@Override
	public Event[] getRegisteredEvents()
	{
		return new Event[]
		{ Event.IRC_PRIVMSG };
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		if (this._parent.getParent().isDebugging())
		{
			if (ev == Event.IRC_PRIVMSG)
			{
				String msg = args.getParamAsString("message");
				if (msg.equalsIgnoreCase("!numcms"))
				{
					int countAlive = 0;
					for (ConnectionManager cm : this._mans)
					{
						if (cm.isAlive())
						{
							countAlive++;
						}
					}

					this._parent
							.getParent()
							.sendPrivMsg(
									args.getParamAsString("sendto"),
									"There are "
											+ this._mans.size()
											+ " connection managers registered (the number that have ran at any point). On the other hand, there are "
											+ countAlive
											+ " connection managers activly managing connections.");
				}
			}
		}
	}

	private ServerSocket _ss;
	private final NoIRCModule _parent;
	private final List<ConnectionManager> _mans = new ArrayList<ConnectionManager>();
}

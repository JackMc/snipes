package plugins.irchook;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;

public class IRCHook extends Module implements Runnable
{
	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		if (args.containsKey("line"))
		{
			for (Hook ps : this._hooks)
			{
				synchronized (this._hooks)
				{
					ps.line(args.getParamAsString("line"));
				}
			}
		}
	}

	public void addHook(Hook h)
	{
		h.init();
		synchronized (this._hooks)
		{
			this._hooks.add(h);
		}
	}

	@Override
	protected ModuleReturn snipesInit()
	{
		try
		{
			int i = this.getParent().getConfiguration().getPropertyAsInteger(
					"hookprt", 4031);
			this._server = new ServerSocket(i);
		} catch (IOException e)
		{
			this.setError("IOException while initialising server: "
					+ e.getMessage());
			return ModuleReturn.ERROR;
		}

		Thread t = new Thread(this, "IRCHook main thread");
		t.setDaemon(true);
		t.start();
		// See if we should log.
		if (this.getParent().getConfiguration().getPropertyAsBoolean("log",
				false))
		{
			this.addHook(new LogHook());
		}
		return null;
	}

	@Override
	public void run()
	{
		// This thread just constantly is checking for new connections.
		while (this.getParent().isConnected())
		{
			try
			{
				this.addHook(new SocketHook(this._server.accept()));
			} catch (IOException e)
			{
				if (!this._server.isClosed())
				{
					System.err
							.println("IRCHook: Unexpected error accepting connection, trying again.");
				}
				else
				{
					System.err
							.println("IRCHook: Server socket channel has been closed. Exiting.");
					return;
				}
			}
		}
	}

	@Override
	public void snipesFini()
	{
		for (Hook h : this._hooks)
		{
			h.fini();
		}
		try
		{
			this._server.close();
		} catch (IOException e)
		{
			// We don't care.
		}
	}

	ServerSocket _server;
	List<Hook> _hooks = new ArrayList<Hook>();
}

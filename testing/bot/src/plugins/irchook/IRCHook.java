package plugins.irchook;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
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
			for (PrintStream ps : this._channels)
			{
				ps.println(args.getParamAsString("line"));
			}
		}
	}

	@Override
	protected ModuleReturn snipesInit()
	{
		try
		{
			this._server = new ServerSocket(this.getParent().getConfiguration()
					.getPropertyAsInteger("hookprt", 4000));
		} catch (IOException e)
		{
			System.err
					.println("IRCHook: Cannot initialise, server initialisation failure.");
			this.setError("IOException while initialising server: "
					+ e.getMessage());
			return ModuleReturn.ERROR;
		}

		Thread t = new Thread(this, "IRCHook main thread");
		t.setDaemon(true);
		t.start();
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
				Socket sc = this._server.accept();
				this._channels.add(new PrintStream(sc.getOutputStream()));
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

	ServerSocket _server;
	List<PrintStream> _channels = new ArrayList<PrintStream>();
}

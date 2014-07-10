package plugins.bouncer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.ossnipes.snipes.bot.Configuration;
import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.lib.irc.BotConstants;

public class BouncerMain extends Thread
{
	public BouncerMain(SnipesBot bot)
	{
		this._bot = bot;
		// So we don't hold up the bot exiting.
		this.setDaemon(true);
		this.start();
	}

	@Override
	public void run()
	{
		// To accept client connections.
		ServerSocket sock;
		int accPort = BotConstants.IRC_DEFAULT_PORT;

		Configuration c = this._bot.getConfiguration();

		Integer bport = c.getPropertyAsInteger("bport");

		if (bport != null)
		{
			accPort = bport;
		}

		try
		{
			sock = new ServerSocket(accPort);
		} catch (IOException e)
		{
			System.err
					.println("Snipes bouncer: unable to bind to port "
							+ accPort
							+ " read from "
							+ (bport != null ? "configuration file."
									: "default value.")
							+ " The bouncer will not load.");
			return;
		}

		while (true)
		{
			try
			{
				Socket sockAccd = sock.accept();
				BouncerConnection bc = new BouncerConnection(this._bot,
						sockAccd);
				// Add it as a event listener
				this._bot.addEventListener(bc);
				// Add it to the list of connections.
				this._conns.add(bc);
			} catch (IOException e)
			{
				System.err.println("Snipes bouncer: unable to accept socket.");
			}
		}
	}

	private final List<BouncerConnection> _conns = new ArrayList<BouncerConnection>();
	private final SnipesBot _bot;
}

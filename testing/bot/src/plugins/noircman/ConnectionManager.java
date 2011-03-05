package plugins.noircman;

import java.io.IOException;

import org.ossnipes.snipes.bot.Configuration;
import org.ossnipes.snipes.lib.irc.BotUtils;
import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCEventListener;

public class ConnectionManager extends Thread implements IRCEventListener
{
	private boolean running = true;

	public ConnectionManager(Connection c, NoIRCModule parent)
	{
		if (c == null)
		{
			throw new IllegalArgumentException("c cannot be null.");
		}
		this._c = c;
		this._parent = parent;
		this.start();
	}

	@Override
	public void run()
	{
		this._parent.addEventListener(this);

		try
		{
			if (this.checkAuth())
			{
				this.startLoop();
			}
		} catch (Exception e)
		{
			// A I/O operation gave us a :(.
		}
	}

	private boolean checkAuth()
	{
		this._c.sendln("HELLO");
		try
		{
			String resp = this._c.recv();
			if (resp == null)
			{
				return false;
			}
			if (resp.equalsIgnoreCase("HELLO"))
			{
				String user = this._c.recv();
				String pass = this._c.recv();
				boolean correctPass = false;
				if (user == null || pass == null)
				{
					return false;
				}
				if (pass.equals("-") || pass.isEmpty())
				{
					correctPass = this.tryID(user, null);
				}
				else
				{
					correctPass = this.tryID(user, pass);
				}

				if (!correctPass)
				{
					this._c.sendln("GOODBYE");
					this.cleanupConnection();
					return false;
				}
				else
				{
					this._c.sendln("WELCOME");
				}
			}
		} catch (IOException e)
		{
			this.cleanupConnection();
			return false;
		}

		return true;
	}

	private void cleanupConnection()
	{
		this._c.close();
	}

	private String startLoop()
	{
		String cmdPrev = null;
		String cmd = null;
		String result = cmd;
		while (this.running)
		{
			cmdPrev = cmd;
			try
			{
				cmd = this._c.recv();
			} catch (IOException e)
			{
				continue;
			}
			if (cmd == null)
			{
				result = cmdPrev;
				break;
			}

			String[] cmdTok = cmd.split(" ");

			if (cmd.equalsIgnoreCase("SAYHELLO"))
			{
				this._c.sendln("PRNT");
				this._c.sendln("hello world :)");
			}
			else if (cmdTok[0].equalsIgnoreCase("FLAG"))
			{
				if (cmdTok.length != 3)
				{
					this._c.sendln("INVALIDARGS");
				}
				this.flags.put(cmdTok[1], cmdTok[2]);
			}

			result = cmd;
			cmd = null;
		}
		// Send back their last command.
		return result;
	}

	private boolean tryID(String user, String pass)
	{
		String[] users = this._parent.getConfiguration()
				.getPropertyAsStringArray("noircusers", new String[] {});
		String[] passes = this._parent.getConfiguration()
				.getPropertyAsStringArray("noircpasses", new String[] {});
		String p2 = pass == null ? "" : pass;

		int inU = BotUtils.arrayIndex(users, user);
		int inP = BotUtils.arrayIndex(passes, p2);

		if (inP == -1 || inU == -1)
		{
			return false;
		}

		if (inU == inP)
		{
			return true;
		}

		return false;
	}

	public void requestStop()
	{
		this.running = false;
		this.cleanupConnection();
	}

	@Override
	public Event[] getRegisteredEvents()
	{
		Boolean flagVal = this.flags.getPropertyAsBoolean("IRCR");
		return flagVal == null ? null : (flagVal ? Event.values() : null);
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		this._c.sendln((new StringBuilder("IRCLN")).append(" ")
				.append(args.getParam("line")).toString());
	}

	private final Connection _c;
	private final NoIRCModule _parent;
	private final Configuration flags = new Configuration();
}

package plugins.noircman;

import java.io.IOException;

import org.ossnipes.snipes.lib.irc.BotUtils;

public class ConnectionManager extends Thread
{
	private boolean running = true;

	public ConnectionManager(Connection c, NoIRCModule parent)
	{
		System.err.println("NoIRCMan ConnectionManager: starting up.");
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
		this._c.sendln("HELLO");
		try
		{
			String resp = this._c.recv();
			if (resp == null)
			{
				return;
			}
			if (resp.equalsIgnoreCase("HELLO"))
			{
				String user = this._c.recv();
				String pass = this._c.recv();
				boolean correctPass = false;
				if (user == null || pass == null)
				{
					return;
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
					this._c.close();
					return;
				}
				else
				{
					this._c.sendln("WELCOME");
				}
			}

		} catch (IOException e)
		{
			this._c.close();
			return;
		}
		try
		{
			this.startLoop();
		} catch (Exception e)
		{
			// A I/O operation gave us a :(.
		}
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
			System.err.println(cmd);
			if (cmd == null)
			{
				result = cmdPrev;
				break;
			}
			if (cmd.equals("SAYHELLO"))
			{
				this._c.sendln("PRNT");
				this._c.sendln("hello world :)");
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
		this._c.close();
	}

	private final Connection _c;
	private final NoIRCModule _parent;
}

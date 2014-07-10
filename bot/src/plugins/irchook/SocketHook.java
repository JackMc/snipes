package plugins.irchook;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class SocketHook implements Hook
{

	public SocketHook(Socket s)
	{
		this._s = s;
		try
		{
			this._ps = new PrintStream(this._s.getOutputStream());
		} catch (IOException e)
		{
			System.err
					.println("IRCHook: Unable to initialise socket PrintStream: "
							+ e.getMessage());
		}
	}

	@Override
	public boolean init()
	{
		return true;
		//
	}

	@Override
	public void line(String line)
	{
		this._ps.println(line);
	}

	@Override
	public void fini()
	{
		try
		{
			this._s.close();
		} catch (IOException e)
		{
			// We don't care.
		}
	}

	Socket _s;
	PrintStream _ps;
}

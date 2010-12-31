package org.ossnipes.snipes.lib.irc;

import java.io.IOException;
import java.net.UnknownHostException;

public class ExampleBot extends IRCBase
{
	public static void main(String args[])
	{
		new ExampleBot().run();
	}
	public void run()
	{
		try
		{
			connect("irc.geekshed.net");
			join("#Snipes");
			setVerbose(true);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		
		
	}
}

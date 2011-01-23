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
			setVerbose(true);
			connect("irc.geekshed.net");
			join("#Snipes");
			join("#Snipes-Testing");
            new TestHandler(this);
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
		// Switch through the events.
        switch (ev) {
            case IRC_PRIVMSG: {
                break;
            }
            case IRC_PING:
                break;
            case IRC_JOIN_TOPIC:
                break;
            case IRC_TOPIC:
                break;
        }
	}
}

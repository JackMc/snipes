package org.ossnipes.snipes.lib.irc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

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
            join("#Bots");
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
                System.err.println("Got a PRIVMSG.");
                System.err.println("MSG: " + (String)args.getParam("message") + " startsWith: " + ((String)args.getParam("message")).startsWith("!say"));
                if  (((String)args.getParam("message")).startsWith("!say"))
                {
                    StringTokenizer stringTokenizer = new StringTokenizer((String)args.getParam("message"));
                    String msg = "";
                    int counter = 0;
                    while (stringTokenizer.hasMoreTokens())
                    {
                        if (counter != 1)
                        {
                            String s = stringTokenizer.nextToken();
                            msg += (counter != 2 ? " " : "") + s;
                        }
                        counter ++;
                    }
                    sendPrivMsg((String)args.getParam("to"), "MSG: " + msg);
                }

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

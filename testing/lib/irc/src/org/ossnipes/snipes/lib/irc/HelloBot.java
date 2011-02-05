package org.ossnipes.snipes.lib.irc;

import java.io.IOException;

public class HelloBot {
	public static void main(String args[]) throws IOException {
		new IRCBase()
		{

			@Override
			public void handleEvent(Event ev, EventArgs args) {
				switch (ev)
				{
				case IRC_PRIVMSG:
					if (args.getParamAsString("message").equalsIgnoreCase("!hi"))
					{
						sendPrivMsg(args.getParamAsString("to"), "Hello to you too, " + args.getParamAsString("from"));
					}
					break;
				}
			}
		}.setNick("HWBot").connect("irc.freenode.net").join("#botters");
	}
}

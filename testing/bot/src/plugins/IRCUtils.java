package plugins;

import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.lib.irc.Channel;
import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;

public class IRCUtils extends Module
{

	@Override
	protected ModuleReturn snipesInit()
	{
		return null;
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		if (ev == Event.IRC_PRIVMSG)
		{
			String sendTo = args.getParamAsString("sendto");
			String msg = args.getParamAsString("message");
			String[] msgSplit = msg.split(" ");

			if (msgSplit[0].equalsIgnoreCase("!join"))
			{
				if (msgSplit.length < 2)
				{
					this.getParent().sendPrivMsg(sendTo,
							"Use like !join <channel>");
					return;
				}
				else
				{
					this.getParent().sendPrivMsg(sendTo,
							"Joining channel " + msgSplit[1]);
					this.getParent().join(msgSplit[1]);
				}
			}
			else if (msgSplit[0].equalsIgnoreCase("!part"))
			{
				if (msgSplit.length < 2)
				{
					this.getParent().sendPrivMsg(sendTo,
							"Use like !part <channel>");
					return;
				}
				else
				{
					this.getParent().sendPrivMsg(sendTo,
							"Parting channel " + msgSplit[1]);
					this.getParent().part(msgSplit[1]);
				}
			}
			else if (msg.equalsIgnoreCase("!listchanobjs"))
			{
				StringBuilder sb = new StringBuilder("Channels: ");
				Channel[] chans = this.getParent().getJoinedChannels();
				for (Channel s : chans)
				{
					sb.append(s.getName()
							+ (s.equals(chans[chans.length - 1]) ? "" : ", "));
				}
				this.getParent().sendPrivMsg(sendTo, sb.toString());
			}
			else if (msg.equalsIgnoreCase("!listchantopics"))
			{
				StringBuilder sb = new StringBuilder("Channels: ");
				Channel[] chans = this.getParent().getJoinedChannels();
				for (Channel s : chans)
				{
					sb.append(s.getName() + ": " + s.getTopic()
							+ (s.equals(chans[chans.length - 1]) ? "" : ", "));
				}
				this.getParent().sendPrivMsg(sendTo, sb.toString());
			}
		}
	}

}

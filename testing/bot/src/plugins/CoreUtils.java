package plugins;

import org.ossnipes.snipes.bot.CoreModule;
import org.ossnipes.snipes.bot.ModuleLoadException;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.bot.SnipesConstants;
import org.ossnipes.snipes.lib.irc.BotUtils;
import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;

public class CoreUtils extends CoreModule
{
	@Override
	protected ModuleReturn snipesInit()
	{
		System.err.println("Hello :)");
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

			if (msgSplit.length == 0)
			{

			}

			if (msgSplit[0].equalsIgnoreCase("!load"))
			{
				this.loadModuleCommand(sendTo, msgSplit);
			}
			else if (msgSplit[0].equalsIgnoreCase("!unload"))
			{
				this.unloadModuleCommand(sendTo, msgSplit);
			}
		}
	}

	private void unloadModuleCommand(String sendTo, String[] msgSplit)
	{
		if (msgSplit.length != 2)
		{
			this.getParent().sendPrivMsg(sendTo,
					"Incorrect syntax. Use like !unload mypackage.mymodule");
			return;
		}
		else
		{
			if (msgSplit[1].equals(this.getClass().getName()))
			{
				this.getParent().sendPrivMsg(sendTo,
						"This plugin cannot unload itself.");
				return;
			}
			if (BotUtils.arrayContains(SnipesConstants.CORE_MODULES,
					msgSplit[1]))
			{
				this.getParent().sendPrivMsg(sendTo,
						"Cannot unload a core module.");
				return;
			}
			else if (!this.removeModule(msgSplit[1]))
			{
				this.getParent()
						.sendPrivMsg(
								sendTo,
								"Bot could not unload the specified module because it is not loaded (at least not by this bot).");
				return;
			}
			else
			{
				this.getParent().sendPrivMsg(sendTo, "Module unloaded.");
			}
		}
	}

	private void loadModuleCommand(String sendTo, String[] msgSplit)
	{
		if (msgSplit.length != 2)
		{
			this.getParent().sendPrivMsg(sendTo,
					"Incorrect syntax. Use like !load mypackage.mymodule");
			return;
		}
		else if (msgSplit[1].equalsIgnoreCase(this.getClass().getName()))
		{
			this.getParent().sendPrivMsg(sendTo,
					"This plugin cannot load itself.");
		}

		else
		{
			try
			{
				this.loadModule(msgSplit[1]);
				this.getParent().sendPrivMsg(sendTo, "Module loaded.");
			} catch (ModuleLoadException e)
			{
				this.getParent()
						.sendPrivMsg(
								sendTo,
								"Error: Bot could not load the specified module because it is not a subclass of Module or CoreModule.");
			} catch (InstantiationException e)
			{
				this.getParent()
						.sendPrivMsg(
								sendTo,
								"Error: Bot could not load the specified module because it is a abstract class or some other kind of non-instantiable class.");
			} catch (IllegalAccessException e)
			{
				this.getParent()
						.sendPrivMsg(
								sendTo,
								"Error: Bot could not load module the specified because it's constructor could not be called.");
			} catch (ClassNotFoundException e)
			{
				this.getParent()
						.sendPrivMsg(
								sendTo,
								"Error: Bot could not load the specified module because it could not be found in the bot's CLASSPATH.");
			}
		}
	}
}

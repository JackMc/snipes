package org.ossnipes.snipes.bot;

import org.ossnipes.snipes.lib.irc.BotConstants;

/** Abstraction of Snipes argument parsing.
 * 
 * @author Jack McCracken (Auv5)
 * @since Snipes 0.01 */
public class ArgumentParser implements ArgumentConstants, PropertyConstants,
		BotConstants
{

	/** Gets the ArgumentParser instance.
	 * 
	 * @return The instance. */
	public static ArgumentParser getParser()
	{
		// Call the overload
		return getParser(false);
	}

	/** Gets the ArgumentParser instance.
	 * 
	 * @param debugCreateNew If a new instance should be created. This is used
	 *            for debug purposes.
	 * @return The instance, new if debugCreateNew is true. */
	public static ArgumentParser getParser(boolean debugCreateNew)
	{
		// See if we need to create a new instance.
		return debugCreateNew ? new ArgumentParser() : _inst;
	}

	/** Parses the arguments passed in, acting on the given
	 * {@link org.ossnipes.snipes.bot.Configuration} Object.
	 * 
	 * @param c The {@link org.ossnipes.snipes.bot.Configuration} to act on.
	 * @param args The arguments to parse, generally from main(String[] args) */
	public void parseArgs(Configuration c, String[] args)
	{
		int i = 0, j;
		String arg;

		while (i < args.length && args[i].startsWith("-"))
		{
			arg = args[i++];

			if (arg.equals(VERBOSE_L_ARG_NAME))
			{
				this.onVerbose(c);
			}

			else if (arg.equals(DEFINE_L_ARG_NAME))
			{
				if (i < args.length)
				{
					this.onDefine(c, arg, args[i++]);
				}
				else
				{
					System.err.println(DEFINE_L_ARG_NAME
							+ " requires a property=value argument.");
					break;
				}
			}
			else if (arg.equals(VERSION_L_ARG_NAME))
			{
				this.printVersion();
			}
			// Check for small flag args.
			else
			{
				for (j = 0; j < arg.length(); j++)
				{
					char flag = arg.charAt(j);
					switch (flag)
					{
						case ARG_PREFIX:
						{
							// The initial -
							break;
						}
						case VERBOSE_S_ARG_NAME:
						{
							// Verbose on
							this.onVerbose(c);
							break;
						}
						case DEFINE_S_ARG_NAME:
						{
							if (i < args.length)
							{
								this.onDefine(c, arg, args[i++]);
							}
							else
							{
								System.err
										.println(ARG_PREFIX
												+ DEFINE_S_ARG_NAME
												+ " requires a property=value argument.");
								break;
							}
							break;
						}
						default:
							System.err.println("Illegal option " + flag);
						break;
					}
				}
			}
		}
		if (i != args.length)
		{
			System.exit(Exit.EXIT_INCORRECTARGSSYN.ordinal());
		}
	}

	/** Prints out the version of the bot and the Snipes IRC Framework. */
	private void printVersion()
	{
		System.out
				.println("Snipes IRC bot, part of the Open Source Snipes Project.");
		System.out.println("This is version "
				+ SnipesConstants.SNIPESBOT_VERSTR + " of the Snipes IRC Bot.");
		System.out
				.println("This IRC bot makes use of the Snipes IRC API. The API this bot is currently using is at version "
						+ SNIRC_VERSION_STRING);
		System.exit(Exit.EXIT_NORMAL.ordinal());
	}

	/** Defines a property on the given {@link Configuration} Object.
	 * 
	 * @param c The {@link Configuration} to act on.
	 * @param key The key to set.
	 * @param value The value to set it to. */
	private void define(Configuration c, String key, String value)
	{
		c.setProperty(key, value);
	}

	/** Sets the bot's verboseness on.
	 * 
	 * @param c The {@link Configuration} to act on. */
	private void onVerbose(Configuration c)
	{
		this.define(c, VERBOSE_PROP_NAME, "TRUE");
	}

	/** Parses a define command.
	 * 
	 * @param c The {@link Configuration} to act on.
	 * @param arg The original first argument, used for error printing.
	 * @param arg2 The argument after arg.
	 * @return True if there was no syntax errors, false otherwise. */
	private boolean onDefine(Configuration c, String arg, String arg2)
	{
		String[] eqSplit = arg2.split("=");
		if (eqSplit.length != 2)
		{
			System.err
					.println("Define directive: Argument invalid. Argument should look like: \""
							+ arg + " myproperty=myvalue\"");
			return false;
		}
		else
		{
			this.define(c, eqSplit[0], eqSplit[1]);
			return true;
		}
	}

	private static final ArgumentParser _inst = new ArgumentParser();
}

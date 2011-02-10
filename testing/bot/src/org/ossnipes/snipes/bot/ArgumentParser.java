package org.ossnipes.snipes.bot;

public class ArgumentParser
{
	private static final ArgumentParser inst = new ArgumentParser();

	public static ArgumentParser getParser()
	{
		return inst;
	}

	public static ArgumentParser getParser(boolean debugCreateNew)
	{
		return debugCreateNew ? new ArgumentParser() : inst;
	}

	public void parseArgs(Configuration c, String[] args)
	{
		int i = 0, j;
		String arg;

		while (i < args.length && args[i].startsWith("-"))
		{
			arg = args[i++];

			// use this type of check for "wordy" arguments
			if (arg.equals("--verbose"))
			{
				this.onVerbose(c);
			}

			// use this type of check for arguments that require arguments
			else if (arg.equals("--define"))
			{
				if (i < args.length)
				{
					this.onDefine(c, arg, args[i++]);
				}
				else
				{
					System.err
							.println("--define requires a property=value argument.");
					break;
				}

			}

			// Check for small flag args.
			else
			{
				for (j = 0; j < arg.length(); j++)
				{
					char flag = arg.charAt(j);
					switch (flag)
					{
						case '-':
						{
							// The initial -
							break;
						}
						case 'v':
						{
							// Verbose on
							this.onVerbose(c);
							break;
						}
						case 'D':
						{
							if (i < args.length)
							{
								this.onDefine(c, arg, args[i++]);
							}
							else
							{
								System.err
										.println("--define requires a property=value argument.");
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

	private void define(Configuration c, String key, String value)
	{
		c.setTempProperty(key, value);
	}

	private void onVerbose(Configuration c)
	{
		this.define(c, "verbose", "TRUE");
	}

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
}

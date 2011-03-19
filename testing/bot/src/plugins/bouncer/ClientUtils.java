package plugins.bouncer;

public class ClientUtils
{
	public static boolean isMessageToMe(PseudoClient client, String line)
	{
		String[] msgSplit = line.split(" ");

		if (msgSplit.length == 0)
		{
			// Impossible case.
			throw new InternalError();
		}

		// Woo, long expression!
		return msgSplit.length >= 3 && msgSplit[0].equalsIgnoreCase("PRIVMSG")
				&& msgSplit[1].equalsIgnoreCase(client.getNick());
	}

	public static boolean isMessage(String line, boolean fromClient)
	{
		String[] msgSplit = line.split(" ");

		for (String s : msgSplit)
		{
			System.err.println("HERRO: " + s);
		}
		if (msgSplit.length == 0)
		{
			// Impossible case.
			throw new InternalError();
		}

		return msgSplit.length >= (fromClient ? 3 : 4)
				&& msgSplit[(fromClient ? 0 : 1)].equalsIgnoreCase("PRIVMSG");
	}
}

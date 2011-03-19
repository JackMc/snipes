package plugins.bouncer;

/** Contains a list of pseudo-clients the bot should query with lines (in order).
 * 
 * @author Jack McCracken (Auv5) */
public enum EnumPseudoClient
{
	// Order matters!
	BOT_CLIENT(BotClient.class), IRC_CLIENT(IRCClient.class);

	EnumPseudoClient(Class<? extends PseudoClient> clazz)
	{
		if (clazz == null)
		{
			throw new IllegalArgumentException("clazz cannot be null.");
		}

		this._clazz = clazz;
	}

	public Class<? extends PseudoClient> getClientClass()
	{
		return this._clazz;
	}

	private Class<? extends PseudoClient> _clazz;
}

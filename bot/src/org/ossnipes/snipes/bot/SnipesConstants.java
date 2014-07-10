package org.ossnipes.snipes.bot;

/** Contains all of the general constants for SnipesBot.
 * 
 * @author Jack McCracken (Auv5) */
public interface SnipesConstants
{
	public static final float SNIPESBOT_VERSION = 0.01f;
	public static final String SNIPESBOT_VERSTR = Float
			.toString(SNIPESBOT_VERSION);
	public static final String CONFIGURATION_FILENAME = "snipes.properties";
	public static final String[] CORE_MODULES =
	{ "plugins.CoreUtils", "plugins.irchook.IRCHook" };
	public static final String DEV_EMAIL = "jack.mccracken@ymail.com";
}

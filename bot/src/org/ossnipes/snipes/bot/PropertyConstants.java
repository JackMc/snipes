package org.ossnipes.snipes.bot;

import org.ossnipes.snipes.lib.irc.BotConstants;

/** Contains all of the names of the properties in the bot. It is preferable to
 * use this class rather than String literals because property names may change.
 * 
 * @author Jack McCracken (Auv5) */
public interface PropertyConstants extends BotConstants
{
	static final String SERVER_PROP_NAME = "server";
	static final String SERVER_PROP_DEFAULT = "irc.geekshed.net";

	static final String NICK_PROP_NAME = "nick";
	static final String NICK_PROP_DEFAULT = DEFAULT_NICK;

	static final String REALNAME_PROP_NAME = "realname";
	static final String REALNAME_PROP_DEFAULT = DEFAULT_REALNAME;

	static final String CHANNELS_PROP_NAME = "channels";
	static final String[] CHANNELS_PROP_DEFAULTS =
	{ "#Snipes-Testing" };

	static final String MODULES_PROP_NAME = "modules";
	static final String[] MODULES_PROP_DEFAULTS = {};

	static final String PORT_PROP_NAME = "port";
	static final int PORT_PROP_DEFAULT = IRC_DEFAULT_PORT;

	static final String VERBOSE_PROP_NAME = "verbose";
	static final boolean VERBOSE_PROP_DEFAULT = false;

	static final String DEBUG_PROP_NAME = "debug";
	static final boolean DEBUG_PROP_DEFAULT = false;

	static final String ALT_CONF_PROP_NAME = "altconf";
	static final String ALT_CONF_PROP_DEFAULT = SnipesConstants.CONFIGURATION_FILENAME;
}

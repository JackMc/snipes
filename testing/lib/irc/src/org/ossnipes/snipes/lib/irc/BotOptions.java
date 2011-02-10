package org.ossnipes.snipes.lib.irc;

class BotOptions
{
	// Both initialised to false.
	// Both volatile because both threads access them.
	static volatile boolean VERBOSE;
	static volatile boolean DEBUG;
}

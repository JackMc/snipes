package org.ossnipes.snipes.bot;

public enum Exit
{
	/** Indicates that Snipes exited under normal conditions. */
	EXIT_NORMAL,
	/** Indicates that the argument syntax was incorrect, and Snipes is exitting. */
	EXIT_INCORRECTARGSSYN,
	/** Indicates that Snipes was unable to load the configuration file, and is
	 * exiting. */
	EXIT_CONFIGNOLOAD,
	/** Indicates that Snipes could not connect to the specified server, and is
	 * exiting. */
	EXIT_COULDNOTCONNECT,
	/** Indicates that this JVM's version is too low for Snipes to operate on. */
	EXIT_JVM_VERSION_TOO_LOW,
	/** Indicates that a copy of the Snipes IRC library could not be found on the
	 * CLASSPATH, and that Snipes is exiting. */
	EXIT_NO_SNIRC
}

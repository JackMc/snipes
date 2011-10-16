package org.ossnipes.snipes.lib.irc;

interface InternalConstants 
{
	/** True if you wish for the API to favour allowing event handlers removing others (or themselves) over speed (not that much of a increase) during event sending.*/
	static final boolean USE_EVLIST_COPY = true;
	/** True if you wish for the API to track what channels users are in. */
	static final boolean CHANNEL_TRACKING = true;
}

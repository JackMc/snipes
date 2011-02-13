package org.ossnipes.snipes.bot;

/** Contains the names and default values of several Snipes arguments.
 * 
 * @author Jack McCracken (Auv5)
 * 
 * @since Snipes 0.6 */
interface ArgumentConstants
{
	public static final char ARG_PREFIX = '-';
	public static final String WORDY_ARG_PREFIX = ARG_PREFIX
			+ String.valueOf(ARG_PREFIX);

	public static final String DEFINE_L_ARG_NAME = WORDY_ARG_PREFIX + "define";
	public static final char DEFINE_S_ARG_NAME = 'D';
	public static final String VERBOSE_L_ARG_NAME = WORDY_ARG_PREFIX
			+ "verbose";
	public static final char VERBOSE_S_ARG_NAME = 'v';
	public static final String VERSION_L_ARG_NAME = WORDY_ARG_PREFIX
			+ "version";
}

package org.ossnipes.snipes.bot;

/** Represents a permission that it is possible for a module to be loaded with.
 * 
 * @author Jack McCracken (Auv5) */
public enum ModulePermission
{
	/** Represents the ability to hook (subscribe to) events other than the
	 * Events in the default {@link Event} enumeration. */
	CAN_HOOK_INTERNAL,
	/** Represents the ability to intercept events before they get to the events
	 * loaded after it. */
	CAN_CANCEL_EVENTS
}

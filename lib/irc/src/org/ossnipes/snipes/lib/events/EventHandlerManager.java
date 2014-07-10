package org.ossnipes.snipes.lib.events;

interface EventHandlerManager {

	/** Adds a event to the list of subscribed events.
	 * 
	 */
	public abstract void addEvent(Event ev);

	/** Adds a event array to the list of subscribed events.
	 * 
	 */
	public abstract void addEvent(Event[] evs);

	/**
	 * Removes an event from the list of subscribed events.
	 * @param ev The event to be removed from the list.
	 * @return True if an event was removed, false otherwise.
	 */
	public abstract boolean removeEvent(Event ev);

	/**
	 * Checks if the managed object is subscribed to a given event.
	 * @param ev The events.
	 * @return 
	 */
	public abstract boolean isSubscribed(Event ev);

	public abstract void handleEvent(Event ev, EventArgs args);

	public abstract boolean isIRCBase();

	public abstract void handleInternalEvent(Event ev, EventArgs args);

	public abstract void sendEvent(Event ev, EventArgs args);

	public abstract Event[] registerInitialEvents();

	public abstract Event[] getRegisteredEvents();

}
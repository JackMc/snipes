package org.ossnipes.snipes.lib.irc.test;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ossnipes.snipes.lib.events.BotUtils;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;
import org.ossnipes.snipes.lib.events.EventHandlerCollection;
import org.ossnipes.snipes.lib.events.IRCEventListener;


public class TestEventHandlerManager extends TestCase
{
	private boolean tDone;
	
	@Before
	public void setUp()
	{
		_t = new TestEventHandler();
		_ehc = new EventHandlerCollection();
		_ehc.addEventListener(_t);
	}
	
	@Test
	public void testEvents()
	{
		BotUtils.sendEvent(new EventArgs(Event.IRC_JOIN, "JOIN #Snipes"), _ehc);
		// Wait 5 seconds for the event to propagate (a nice and big value)
		if (!waitForEvent(5000))
		{
			fail("Event handler did not respond in five seconds.");
		}
	}
	
	public boolean waitForEvent(int waitTime)
	{
		long start = System.nanoTime();
		long now = System.nanoTime();
		long dist = now - start;
		while (!TestEventHandlerManager.this.tDone && dist >= waitTime)
		{
			dist = now - start;
			now += dist;
		}
		
		if (dist >= waitTime)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@After
	public void tearDown()
	{
		tDone = false;
		_ehc.removeEventListener(_t);
	}
	
	private class TestEventHandler implements IRCEventListener
	{

		@Override
		public Event[] getRegisteredEvents() {
			return Event.values();
		}

		@Override
		public void handleEvent(Event ev, EventArgs args) {
			tDone = true;
			System.err.println(ev);
		}
	}
	
	private TestEventHandler _t;
	private EventHandlerCollection _ehc;
}

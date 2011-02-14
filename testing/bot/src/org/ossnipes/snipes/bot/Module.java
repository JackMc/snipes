package org.ossnipes.snipes.bot;

import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCEventListener;

public abstract class Module implements IRCEventListener
{
	/** Performs startup operations for this plugin */
	void initiailise(SnipesBot parentBot, ModuleManager parentManager)
	{
		if (parentBot == null || parentManager == null)
		{
			throw new IllegalArgumentException(
					"parentBot or parentManager cannot be null.");
		}
		this._parentBot = parentBot;
		this._parentManager = parentManager;

		this.addToEvListeners();

		this.snipesInit();
	}

	private void addToEvListeners()
	{
		this._parentBot.addEventListener(this);
	}

	// Begin abstract method definitions
	protected abstract PluginReturn snipesInit();

	protected abstract void snipesFini(PluginExitState status);

	// This one's here for clarity.
	@Override
	public abstract void handleEvent(Event ev, EventArgs args);

	// End abstract method definitions.

	public SnipesBot getBot()
	{
		return this._parentBot;
	}

	public Configuration getConfiguration()
	{
		return this._parentBot.getConfiguration();
	}

	private SnipesBot _parentBot;
	private ModuleManager _parentManager;
}

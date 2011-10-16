package org.ossnipes.snipes.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;
import org.ossnipes.snipes.lib.events.IRCEventListener;

public abstract class Module implements IRCEventListener
{
	public Module()
	{
		// Set all permissions to false, stopping NullPointerExceptions.
		for (ModulePermission mp : ModulePermission.values())
		{
			this._permissions.put(mp, false);
		}
	}

	/**
	 * Performs startup operations for this module
	 * 
	 * @return
	 */
	final ModuleReturn initiailise(SnipesBot parentBot)
	{
		// TODO: Implement dependencies.
		if (parentBot == null)
		{
			throw new IllegalArgumentException("parentBot cannot be null.");
		}
		this._parentBot = parentBot;
		try
		{
			this.checkDeps();
		} catch (ClassNotFoundException e)
		{
			return ModuleReturn.ERROR;
		}
		this.addToEvListeners();

		ModuleReturn mr = this.snipesInit();

		return mr != null ? mr : ModuleConstants.DEFAULT_RETURN;
	}

	private final void addToEvListeners()
	{
		this._parentBot.addEventListener(this);
	}

	// Begin abstract method definitions
	protected abstract ModuleReturn snipesInit();

	// protected abstract void snipesFini(PluginExitState status);

	// This one's here for clarity.
	@Override
	public abstract void handleEvent(Event ev, EventArgs args);

	// End abstract method definitions.

	protected final SnipesBot getParent()
	{
		return this._parentBot;
	}

	protected String[] registerDependancies()
	{
		// No dependencies by default.
		return new String[] {};
	}

	private void checkDeps() throws ClassNotFoundException
	{
		String[] deps = this.registerDependancies();
		for (String m : deps)
		{
			Class.forName(m);
		}
	}

	Class<?>[] getDeps()
	{
		return this._deps.toArray(new Class<?>[this._deps.size()]);
	}

	protected final Configuration getConfiguration()
	{
		return this._parentBot.getConfiguration();
	}

	@Override
	public Event[] getRegisteredEvents()
	{
		return Event.values();
	}

	final void destruct(ModuleExitState state)
	{
		// State will be used later
		this._parentBot.removeEventListener(this);
	}

	public final boolean hasPermission(ModulePermission mp)
	{
		if (mp == null)
		{
			throw new IllegalArgumentException("Permission cannot be null.");
		}
		// Will hold the boxed version of the permission until we can be sure
		// that it is not null
		// and a autounboxing operation will not kill us.
		Boolean mpVal = this._permissions.get(mp);
		return mpVal == null ?
		// Impossible case, as all permissions are set to false at construction
		// (See constructor).
		// I could only see this happening if someone modifies the
		// ModulePermission enumeration to create instances of itself.
		false
				: mpVal;

	}

	void setPermission(ModulePermission permission, boolean value)
	{
		if (permission == null)
		{
			throw new IllegalArgumentException("permission cannot be null.");
		}
		this._permissions.put(permission, value);
	}

	/**
	 * Loads a module into the module system so it will start receiving events
	 * at the next turn of the message loop.
	 * 
	 * @param name The name of the module to load.
	 * @return The {@link ModuleManager} Object accosted with that Module.
	 * @throws ModuleLoadException If the Module is not a instance of Module or
	 *             it's subclasses.
	 * @throws InstantiationException If the bot cannot instantiate a Object of
	 *             the specified Module. This generally happens when the class
	 *             is an array class, or an abstract class.
	 * @throws IllegalAccessException If we cannot call the constructor of the
	 *             Module for whatever reason.
	 * @throws ClassNotFoundException If the bot could not find the Module.
	 * @throws ModuleInitException If {@link Module#snipesInit()} returns
	 *             {@link ModuleReturn#ERROR}
	 */
	protected final ModuleManager loadModule(String name)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ModuleInitException
	{
		return this.getParent().loadModule(name);
	}

	/**
	 * Removes a module from the list of loaded modules.
	 * 
	 * @param name The name of the module to unload.
	 * @return If removing the module was successful. If false, this generally
	 *         means that the module is not loaded.
	 */
	protected final boolean removeModule(String name)
	{
		return this.getParent().removeModule(name);
	}

	/**
	 * Returns true if a module is loaded by the bot.
	 * 
	 * @param name The module to check for.
	 * @return True if the module specified by name is loaded. False otherwise.
	 */
	protected final boolean isModuleLoaded(String name)
	{
		return this.getParent().isModuleLoaded(name);
	}

	protected void setError(String error)
	{
		this._lastError = error;
	}

	public String getLastError()
	{
		return this._lastError;
	}

	protected Module getModuleByName(String name)
	{
		return this.getParent().getModuleByName(name);
	}

	protected Class<?> getModuleClass(String name)
	{
		return this.getParent().getModuleByName(name).getClass();
	}

	private final Map<ModulePermission, Boolean> _permissions = new HashMap<ModulePermission, Boolean>();
	private SnipesBot _parentBot;
	private final List<Class<? extends Module>> _deps = new ArrayList<Class<? extends Module>>();
	private String _lastError = null;
}

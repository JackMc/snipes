package org.ossnipes.snipes.bot;

public abstract class CoreModule extends Module
{
	/** Loads a module into the module system so it will start receiving events
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
	 * @throws ClassNotFoundException If the bot could not find the Module. */
	protected ModuleManager loadModule(String name) throws ModuleLoadException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		return this.getParent().loadModule(name);
	}

	/** Removes a module from the list of loaded modules.
	 * 
	 * @param name The name of the module to unload.
	 * @return If removing the module was successful. If false, this generally
	 *         means that the module is not loaded. */
	protected boolean removeModule(String name)
	{
		return this.getParent().removeModule(name);
	}

	/** Returns true if a module is loaded by the bot.
	 * 
	 * @param name The module to check for.
	 * @return True if the module specified by name is loaded. False otherwise. */
	protected boolean isModuleLoaded(String name)
	{
		return this.getParent().isModuleLoaded(name);
	}
}

package org.ossnipes.snipes.bot;

/** This class is a interface to a Snipes module. It contains methods that should
 * not be used by modules, but the bot should be able to use them.
 * 
 * @author Jack McCracken (Auv5)
 * @since Snipes 0.6 */
class ModuleManager
{

	/** Initialises a {@link ModuleManager} Object, creates the underlying
	 * {@link Module} Object, and creates .
	 * 
	 * @param parent The {@link SnipesBot} Object this module is tied to.
	 * @param moduleClsName The String represented fully qualified class name of
	 *            the module to be loaded.
	 * @throws ClassNotFoundException If the class specified does not exist in
	 *             the project's CLASSPATH.
	 * @throws InstantiationException
	 * @throws IllegalAccessException If constructor is otherwise unavailable.
	 * @throws ModuleLoadException If the module is not a instance of
	 *             {@link Module}. */
	ModuleManager(SnipesBot parent, String moduleClsName)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		this._managed = ModuleUtils.getModuleFromName(moduleClsName);
		this._parent = parent;
	}

	/** initialises the {@link Module}. */
	public void initialise()
	{
		synchronized (this._parent)
		{
			this._managed.initiailise(this._parent);
		}
	}

	public boolean hasPermission(ModulePermission p)
	{
		return this._managed instanceof CoreModule;
	}

	private final Module _managed;
	private final SnipesBot _parent;

	public void destruct(ModuleExitState state)
	{
		this._managed.destruct(state);
	}

	@Override
	public String toString()
	{
		return this.getClass().getName();
	}

	// public void destruct(PluginExitState state)
	// {
	// this._managed.snipesFini(state);
	// }
}

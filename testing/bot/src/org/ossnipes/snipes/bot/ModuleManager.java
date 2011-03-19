package org.ossnipes.snipes.bot;

/** This class is a interface to a Snipes module. It contains methods that should
 * not be used by modules, but the bot should be able to use them.
 * 
 * @author Jack McCracken (Auv5)
 * @since Snipes 0.01 */
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
		// Load and store the module Object.
		this._managed = ModuleUtils.getModuleFromName(moduleClsName);
		// Store the parent.
		this._parent = parent;
	}

	/** initialises the {@link Module}. */
	ModuleReturn initialise()
	{
		synchronized (this._parent)
		{
			return this._managed.initiailise(this._parent);
		}
	}

	/** Checks if a module has the specified permission.
	 * 
	 * @param p The permission to check for.
	 * @return If the module has the specified permission. */
	public boolean hasPermission(ModulePermission p)
	{
		// p will be used later.
		return this._managed instanceof CoreModule;
	}

	/** Calls this module's snipesFini() method and performs any other cleanup.
	 * 
	 * @param state The state to signal to the module. */
	void destruct(ModuleExitState state)
	{
		// Call the destructor.
		this._managed.destruct(state);
	}

	String getModuleClassName()
	{
		return this._managed.getClass().getName();
	}

	Module getModule()
	{
		return this._managed;
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		return this.getClass().getName();
	}

	private final Module _managed;
	private final SnipesBot _parent;
}

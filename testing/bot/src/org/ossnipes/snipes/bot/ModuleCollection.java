package org.ossnipes.snipes.bot;

import java.util.ArrayList;
import java.util.List;

/** Abstracts Module removal and adding.
 * 
 * @author Jack McCracken (Auv5) */
class ModuleCollection
{

	/** Constructs a {@link ModuleCollection}
	 * 
	 * @param parent The parent of the modules to be initialised with this
	 *            collection.
	 * @param onLoadModules A list of modules to be loaded automaticly. */
	public ModuleCollection(SnipesBot parent, String[] onLoadModules)
	{
		// Initialise an empty collection
		this();
		// Loop the modules, printing errors as we go.
		for (String module : onLoadModules)
		{
			try
			{
				this.addModule(parent, module);
			} catch (ModuleLoadException e)
			{
				System.err.println("Could not load module \"" + module
						+ "\" because the module is not a subclass of "
						+ Module.class.getCanonicalName());
			} catch (InstantiationException e)
			{
				System.err
						.println("Could not load module \""
								+ module
								+ "\" because it could not be instantiated. "
								+ "Please make sure that the plugin is not a abstract class.");
			} catch (IllegalAccessException e)
			{
				System.err
						.println("Could not load module \""
								+ module
								+ "\" because we do not have access to it's constructor.");
			} catch (ClassNotFoundException e)
			{
				System.err.println("Could not load module \"" + module
						+ "\" because it cannot be found on the CLASSPATH.");
			}
		}
	}

	/** Constructs a empty {@link ModuleCollection}. */
	public ModuleCollection()
	{
		this._modules = new ArrayList<ModuleManager>();
	}

	/** Loads a array of modules.
	 * 
	 * @param parent The parent of these {@link Module}s.
	 * @param module The String representations of the {@link Module}s to be
	 *            loaded.
	 * @throws ModuleLoadException If the Module is not a instance of Module or
	 *             it's subclasses.
	 * @throws InstantiationException If the bot cannot instantiate a Object of
	 *             the specified Module. This generally happens when the class
	 *             is an array class, or an abstract class.
	 * @throws IllegalAccessException If we cannot call the constructor of the
	 *             Module for whatever reason.
	 * @throws ClassNotFoundException If the bot could not find the Module. */
	public void addModules(SnipesBot parent, String[] modules)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		for (String module : modules)
		{
			this.addModule(parent, module);
		}
	}

	/** Loads a array of modules.
	 * 
	 * @param parent The parent of these {@link Module}s.
	 * @param module The module Objects to load.
	 * @throws ModuleLoadException If the Module is not a instance of Module or
	 *             it's subclasses.
	 * @throws InstantiationException If the bot cannot instantiate a Object of
	 *             the specified Module. This generally happens when the class
	 *             is an array class, or an abstract class.
	 * @throws IllegalAccessException If we cannot call the constructor of the
	 *             Module for whatever reason.
	 * @throws ClassNotFoundException If the bot could not find the Module. */
	public void addModules(SnipesBot parent, Module[] modules)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		for (Module m : modules)
		{
			this.addModule(parent, m);
		}

	}

	/** Loads a module.
	 * 
	 * @param parent The parent of this {@link Module}.
	 * @param module The module to load.
	 * @return The {@link ModuleManager} Object of the loaded {@link Module}
	 * @throws ModuleLoadException If the Module is not a instance of Module or
	 *             it's subclasses.
	 * @throws InstantiationException If the bot cannot instantiate a Object of
	 *             the specified Module. This generally happens when the class
	 *             is an array class, or an abstract class.
	 * @throws IllegalAccessException If we cannot call the constructor of the
	 *             Module for whatever reason.
	 * @throws ClassNotFoundException If the bot could not find the Module. */
	public ModuleManager addModule(SnipesBot parent, String module)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		ModuleManager manager = new ModuleManager(parent, module);
		manager.initialise();
		this._modules.add(manager);
		return manager;
	}

	/** Removes a module's loaded status and calls it's destructor.
	 * 
	 * @param module The module name to unload.
	 * @param state The state to signal the module.
	 * @return If the module was loaded in the first place. */
	public boolean removeModule(String module, ModuleExitState state)
	{
		ModuleManager mRight = null;
		for (ModuleManager m : this._modules)
		{
			mRight = m;
		}

		if (mRight == null)
		{
			// It isn't loaded. Return false.
			return false;
		}

		mRight.destruct(state);
		return this._modules.remove(mRight);
	}

	/** Loads a {@link Module} with the specified parent.
	 * 
	 * @param parent The parent of the {@link Module}.
	 * @param module The {@link Module} to load.
	 * @throws ModuleLoadException If the Module is not a instance of Module or
	 *             it's subclasses.
	 * @throws InstantiationException If the bot cannot instantiate a Object of
	 *             the specified Module. This generally happens when the class
	 *             is an array class, or an abstract class.
	 * @throws IllegalAccessException If we cannot call the constructor of the
	 *             Module for whatever reason.
	 * @throws ClassNotFoundException If the bot could not find the Module. */
	void addModule(SnipesBot parent, Module module) throws ModuleLoadException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		this.addModule(parent, module.getClass().getName());
	}

	/** Returns true if a module is loaded.
	 * 
	 * @param name The module to look for.
	 * @return True if the module is loaded. */
	boolean isModuleLoaded(String name)
	{
		for (ModuleManager s : this._modules)
		{
			if (s.toString().equals(name))
			{
				return true;
			}
		}
		return false;
	}

	List<ModuleManager> _modules;
}

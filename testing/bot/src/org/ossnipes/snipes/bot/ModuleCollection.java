package org.ossnipes.snipes.bot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstracts Module removal and adding.
 * 
 * @author Jack McCracken (Auv5)
 */
class ModuleCollection implements Iterable<ModuleManager>
{

	/**
	 * Constructs a {@link ModuleCollection}
	 * 
	 * @param parent The parent of the modules to be initialised with this
	 *            collection.
	 * @param onLoadModules A list of modules to be loaded automatically.
	 */
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
				// TODO: These error messages need to go in the SnipesBot class.
			} catch (ModuleLoadException e)
			{
				// snipesInit() returned an error.
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
			} catch (ModuleInitException e)
			{
				// System.err.println("Module " + module
				// + "could not be initialised: "
				// + e.getModule().getLastError() != null ? e.getModule()
				// .getLastError() : "No error message specified.");
				Module m = e.getModule();
				System.err.println("Error while initialising module "
						+ module
						+ ". It gave the error \""
						+ (m.getLastError() == null ? "No error specified." : m
								.getLastError()) + "\"");
			}
		}
	}

	/** Constructs a empty {@link ModuleCollection}. */
	public ModuleCollection()
	{
		this._modules = new ArrayList<ModuleManager>();
	}

	/**
	 * Loads a array of modules.
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
	 * @throws ClassNotFoundException If the bot could not find the Module.
	 * @throws ModuleInitException If Module#snies
	 */
	public void addModules(SnipesBot parent, String[] modules)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ModuleInitException
	{
		for (String module : modules)
		{
			this.addModule(parent, module);
		}
	}

	/**
	 * Loads a array of modules.
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
	 * @throws ClassNotFoundException If the bot could not find the Module.
	 * @throws ModuleInitException If {@link Module#snipesInit()} returns
	 *             {@link ModuleReturn#ERROR}
	 */
	public void addModules(SnipesBot parent, Module[] modules)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ModuleInitException
	{
		for (Module m : modules)
		{
			this.addModule(parent, m);
		}

	}

	/**
	 * Loads a module.
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
	 * @throws ClassNotFoundException If the bot could not find the Module.
	 * @throws ModuleInitException If {@link Module#snipesInit()} returns
	 *             {@link ModuleReturn#ERROR}
	 */
	public ModuleManager addModule(SnipesBot parent, String module)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, ModuleInitException
	{
		// TODO: Duplicate testing.
		ModuleManager manager = new ModuleManager(parent, module);

		if (manager.initialise() != ModuleReturn.ERROR)
		{
			this._modules.add(manager);
		}
		else
		{
			// Deinitialise the module safely.
			manager.fini();
			// TODO: Call snipesFini here later if it's needed (we've called
			// their init, so we should call fini).
			String moduleClassName = manager.getModuleClassName();
			// Grammar Nazi ^_^
			throw new ModuleInitException("Module " + moduleClassName
					+ (moduleClassName.endsWith("s") ? '\'' : "'s")
					+ "snipesInit() returned ModuleReturn.ERROR.", manager
					.getModule());
		}

		return manager;
	}

	/**
	 * Removes a module's loaded status and calls it's destructor.
	 * 
	 * @param module The module name to unload.
	 * @param state The state to signal the module.
	 * @return If the module was loaded in the first place.
	 */
	public boolean removeModule(String module, ModuleExitState state)
	{
		ModuleManager mRight = null;
		for (ModuleManager m : this._modules)
		{
			if (m.getModuleClassName().equals(module))
			{
				mRight = m;
			}
		}

		if (mRight == null)
		{
			// It isn't loaded. Return false.
			return false;
		}

		mRight.destruct(state);
		return this._modules.remove(mRight);
	}

	/**
	 * Loads a {@link Module} with the specified parent.
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
	 * @throws ClassNotFoundException If the bot could not find the Module.
	 * @throws ModuleInitException If {@link Module#snipesInit()} returns
	 *             {@link ModuleReturn#ERROR}
	 */
	void addModule(SnipesBot parent, Module module) throws ModuleLoadException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, ModuleInitException
	{
		this.addModule(parent, module.getClass().getName());
	}

	/**
	 * Returns true if a module is loaded.
	 * 
	 * @param name The module to look for.
	 * @return True if the module is loaded.
	 */
	boolean isModuleLoaded(String name)
	{
		return this.getModuleByName(name) == null ? false : true;
	}

	public Module getModuleByName(String name)
	{
		for (ModuleManager s : this._modules)
		{
			if (s.getModuleClassName().equals(name))
			{
				return s.getModule();
			}
		}
		return null;
	}

	@Override
	public Iterator<ModuleManager> iterator()
	{
		return this._modules.iterator();
	}

	private final List<ModuleManager> _modules;
}

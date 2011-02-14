package org.ossnipes.snipes.bot;

import java.util.ArrayList;
import java.util.List;

class ModuleCollection
{

	public ModuleCollection(SnipesBot parent, String[] onLoadModules)
	{
		// Initialise an empty collection
		this();
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

	public ModuleCollection()
	{
		this._modules = new ArrayList<ModuleManager>();
	}

	public void addModules(SnipesBot parent, String[] modules)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		for (String module : modules)
		{
			this.addModule(parent, module);
		}
	}

	public void addModules(SnipesBot parent, Module[] modules)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		for (Module m : modules)
		{
			this.addModule(parent, m);
		}

	}

	public void addModule(SnipesBot parent, String modules)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		ModuleManager manager = new ModuleManager(parent, modules);
		manager.initialise();
		this._modules.add(manager);

	}

	public void addModule(SnipesBot parent, Module module)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		this.addModule(parent, module.getClass().getName());
	}

	List<ModuleManager> _modules;
}

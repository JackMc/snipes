package org.ossnipes.snipes.bot;

public abstract class CoreModule extends Module
{
	protected ModuleManager loadModule(String name) throws ModuleLoadException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException
	{
		return this.getParent().loadModule(name);
	}

	protected boolean removeModule(String name)
	{
		return !this.getParent().removeModule(name);
	}

	protected boolean isModuleLoaded(String name)
	{
		return this.getParent().isModuleLoaded(name);
	}
}

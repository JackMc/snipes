package org.ossnipes.snipes.bot;

public abstract class CoreModule extends Module
{
	protected ClassLoader getModuleClassLoader()
	{
		return ModuleUtils.class.getClassLoader();
	}
}

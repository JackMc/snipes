package org.ossnipes.snipes.bot;

/** Contains utilities for {@link ModuleManager}s.
 * 
 * @author Jack McCracken (Auv5)
 * @since Snipes 0.6 */
/** @author Jack McCracken (Auv5) */
final class ModuleUtils
{
	private ModuleUtils()
	{
	}

	/** Gets the {@link Class} Object from the CLASSPATH for the specified fully
	 * qualified class name, loading it if necessary.
	 * 
	 * @param clsName The fully qualified class name of the class to get the
	 *            {@link Class} Object of.
	 * @return The {@link Class} Object of the specified class name.
	 * @throws ClassNotFoundException If the class could not be found in the
	 *             CLASSPATH. */
	public static Class<?> getClass(String clsName)
			throws ClassNotFoundException
	{
		return Class.forName(clsName);
	}

	/** Creates a {@link Module} Object from a {@link Class} Object, checking
	 * it's superclass, and throwing Exceptions in incorrect cases.
	 * 
	 * @param c The {@link Class} to attempt to create a {@link Module} from.
	 * @return The newly created PluginType.
	 * @throws InstantiationException If the specified module is a abstract
	 *             class, a array class, a primitive type, void, or if the
	 *             constructor is unavailable for some other reason.
	 * @throws ModuleLoadException If the passed in Class does not represent a
	 *             module.
	 * @throws IllegalAccessException If the constructor is otherwise
	 *             unavailable. */
	public static Module getModuleFromClass(Class<?> c)
			throws InstantiationException, ModuleLoadException,
			IllegalAccessException
	{
		if (!Module.class.isAssignableFrom(c))
		{
			throw new ModuleLoadException(
					"Passed in Class does not represent a Module.");
		}
		else
		{
			return (Module) c.newInstance();
		}
	}

	/** Creates a {@link Module} Object from a fully qualified class name,
	 * loading the class if needed.
	 * 
	 * @param plgClsName The fully qualified class name of the {@link Module} to
	 *            load.
	 * @return The {@link Module} instance created.
	 * @throws ModuleLoadException If the specified class is not a subclass of
	 *             {@link Module}.
	 * @throws InstantiationException If the specified module is a abstract
	 *             class, a array class, a primitive type, void, or if the
	 *             constructor is unavailable for some other reason.
	 * @throws IllegalAccessException If the constructor is otherwise
	 *             unavailable.
	 * @throws ClassNotFoundException If the class cannot be found in the
	 *             CLASSPATH. */
	public static Module getModuleFromName(String plgClsName)
			throws ModuleLoadException, InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		return ModuleUtils.getModuleFromClass(ModuleUtils.getClass(plgClsName));
	}
}

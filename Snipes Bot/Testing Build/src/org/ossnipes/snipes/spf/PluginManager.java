package org.ossnipes.snipes.spf;

import org.ossnipes.snipes.exceptions.SnipesPluginException;

public class PluginManager {
    /* Not needed :) */
    public PluginManager() {
    }

    public PluginType loadPlugin(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SnipesPluginException {
        Class<?> pluginClass = Class.forName(name);
        Object pluginObj = pluginClass.newInstance();
        if (!(pluginObj instanceof PluginType)) {
            throw new SnipesPluginException("Plugin is not an instance of 'PluginType'! Please modify your code accordingly ('extends' statement for one of the dirivitives of PluginType (SuperPlugin, Plugin))");
        }
        return (PluginType) pluginObj;
    }

    public PluginType loadPlugin(Class<?> c) throws InstantiationException, IllegalAccessException, SnipesPluginException {
        Class<?> pluginClass = c;
        Object pluginObj = pluginClass.newInstance();
        if (!(pluginObj instanceof PluginType)) {
            throw new SnipesPluginException("Plugin is not an instance of 'PluginType'! Please modify your code accordingly ('extends' statement for one of the dirivitives of PluginType (SuperPlugin, Plugin))");
        }
        return (PluginType) pluginObj;
    }
}

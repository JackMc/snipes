package org.ossnipes.snipes.bot;

import org.ossnipes.snipes.lib.irc.IRCBase;

public abstract class PluginType
{
    private IRCBase parent;
    private PluginManager manager;

    Return init(IRCBase parent, PluginManager manager)
    {
        if (parent == null || manager == null)
        {
            throw new IllegalArgumentException("Null PluginManager or IRCBase.");
        }
        this.parent = parent;
        this.manager = manager;
        return snipesInit();
    }

    protected abstract Return snipesInit();
    protected abstract Return snipesFini();

    Return fini()
    {
        return snipesFini();
    }
}

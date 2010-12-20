package org.ossnipes.snipes.spf;

import org.ossnipes.snipes.enums.PluginConstructRet;
import org.ossnipes.snipes.enums.PluginDestructRet;
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;

import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: jack
 * Date: 19/12/10
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class NoEventPlugin extends PluginType {

    @Override
    protected PluginPassResponse handleEvent(SnipesEvent event, SnipesEventParams params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canHookInternalEvents() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canCancelEvents() {
        return false;
    }

    @Override
    public PluginPassResponse event(SnipesEvent event, SnipesEventParams params)
    {
        return null;
    }
}

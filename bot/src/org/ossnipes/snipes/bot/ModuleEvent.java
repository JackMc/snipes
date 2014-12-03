package org.ossnipes.snipes.bot;

import org.ossnipes.snipes.lib.events.Event;

public class ModuleEvent extends Event {
    public static final ModuleEvent MODULE_LOADED = new ModuleEvent("MODULE_LOADED", Event.EventType.OUTSIDE);
    public static final ModuleEvent MODULE_UNLOADED = new ModuleEvent("MODULE_LOADED", Event.EventType.OUTSIDE);
    
    protected ModuleEvent(String name, Event.EventType type) {
        super(name, type);
    }
}

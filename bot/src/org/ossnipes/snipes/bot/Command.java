package org.ossnipes.snipes.bot;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

public abstract class Command
{
    public Command(SnipesBot bot, Module parent, int minArgs, int maxArgs) {
        this._bot = bot;
        this._minNumArgs = minArgs;
        this._maxNumArgs = maxArgs;
        this._parent = parent;
        this._caps = new HashSet<>();
    }

    public Command(SnipesBot bot, Module parent, int args) {
        this(bot, parent, args, args);
    }

    /**
     * Initialize the command. This method should perform any necessary
     * initialization which depends upon its parent module being fully
     * initialized.
     */
    void init() {}

    /**
     * This method is called when the command is no longer needed.
     * There is no guarantee that this method will be called if
     * the bot experiences abnormal termination. 
     */
    void fini() {}

    /** 
     * Adds a requirement for a specific user capability to use this
     * command. Permissions are implemented as arbitrary strings,
     * defined through module convention alone.
     */
    void requireCapability(Capability capName) {
        _caps.add(capName);
    }

    /**
     * Removes a requirement for a specific user capability to use this
     * command. Permissions are implemented as arbitrary strings,
     * defined through module convention alone.
     */
    void unrequireCapability(Capability capName) {
        _caps.remove(capName);
    }

    /**
     * Returns true if this command needs a specific user capability to
     * execute. 
     */
    boolean needsCapability(Capability capName) {
        return _caps.contains(capName);
    }

    /**
     * Checks if a set of capabilities is acceptable to execute this
     * command.
     */
    boolean accepts(Collection<Capability> capabilities) {
        return capabilities.containsAll(_caps);
    }

    /**
     * Gets the min number of arguments that this command requires. If this
     * is equal to the return of {#getMaxNumArgs()}, then this command
     * has a static number of arguments. 
     */
    int getMinNumArgs() {
        return _minNumArgs;
    }

    /**
     * Gets the max number of arguments that this command requires. If this
     * is equal to the return of {#getMinNumArgs()}, then this command
     * has a static number of arguments. 
     */
    int getMaxNumArgs() {
        return _maxNumArgs;
    }

    abstract void commandExec(String[] args);

    private SnipesBot _bot;
    private Set<Capability> _caps;
    private int _minNumArgs;
    private int _maxNumArgs;
    private Module _parent;
}

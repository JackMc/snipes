package org.ossnipes.snipes.bot;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Capability
{
    static {
        _all = new HashSet<>();
    }
	
    public Capability(String name, Capability[] implies) {
        this._name = name;
        this._implies = new HashSet<>(Arrays.asList(implies));

        _all.add(this);
    }

    public Capability(String name) {
        this(name, new Capability [] {});
    }

    void addImplies(Capability cap) {
        _implies.add(cap);
    }

    public String getName() {
        return _name;
    }

    public int hashCode() {
        return _name.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof Capability) {
            return ((Capability)o).getName() == _name;
        }

        return false;
    }

    public boolean implies(Capability c) {
        return _implies.contains(c);
    }

    public static Capability get(String name) {
        for (Capability c : _all) {
            if (c.getName() == name) {
                return c;
            }
        }

        return new Capability(name);
    }

    private String _name;
    private Set<Capability> _implies;

    private static Set<Capability> _all;
}

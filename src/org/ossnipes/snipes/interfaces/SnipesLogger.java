package org.ossnipes.snipes.interfaces;

import java.util.logging.Level;

public interface SnipesLogger {
	void log(String line);
	void log(String line, Level level);
}

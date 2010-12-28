package org.ossnipes.snipes.main;

import java.io.IOException;

import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.exceptions.SnipesUExceptionHandler;
import org.ossnipes.snipes.tests.TestMain;
import org.ossnipes.snipes.guiconfiguration.main.SnipesConfigurator;
import org.ossnipes.snipes.utils.Configuration;

public class Main {

    public static void main(String args[]) {
        Thread.setDefaultUncaughtExceptionHandler(new SnipesUExceptionHandler());
        boolean CONF = false;
        boolean TEST = false;
        boolean nextIsDArg = false;
        for (String i : args) {
            if (!TEST && (i.equalsIgnoreCase("--makeconf") || i.equals("-m"))) {
                CONF = true;
                new SnipesConfigurator();
            }
            if (!CONF && (i.equalsIgnoreCase("--test") || i.equals("-t"))) {
                try {
                    new TestMain();
                    TEST = true;
                } catch (IOException e) {
                    System.err.println("An unknown I/O error has occurred while running Snipes' tests. The error message was: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.err.println("An ClassNotFoundException occurred while running Snipes' tests. The error message was: " + e.getMessage());
                }
            }
            if (i.equals("-D") || i.equalsIgnoreCase("--define")) {
                nextIsDArg = true;
            }
            if (nextIsDArg) {
                String[] dSplit = i.split("=");
                if (dSplit.length != 2) continue;
                for (int a = 0; a < dSplit.length; a++) {
                    dSplit[a] = dSplit[a].trim();
                }
                Configuration.setTempDirective(dSplit[0],dSplit[1]);
            }
        }
        if (!CONF && !TEST) {
            new SnipesBot();
        }
    }
}

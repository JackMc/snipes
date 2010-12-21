package org.ossnipes.snipes.utils;

/**
 * Contains methods that should be avoided, but are dirty hacks needed for some features (such as making sure that Plugins cannot directly interface with the Snipes instance.)
 */
public class Expensives {

    public static String getCallingMethodInfo() {
        StackTraceElement[] stackTrace;

        try {
            throw new Exception();
        } catch (Exception e) {
            stackTrace = e.getStackTrace();

        }

        if (stackTrace != null && stackTrace.length >= 2) {

            StackTraceElement s = stackTrace[2];
            if (s != null) {

                return s.getClassName() + ".(" + s.getMethodName() + "):[" + s.getLineNumber() + "] -:";

            }
        }

        return null;
    }
}

package plugins.noircchatman;

/**
 * plugins.noircchatman.ConnectionManExceptionHandler
 * This code is licensed under the GNU GPL.
 *
 * @author Jack McCracken (Auv5)
 */
public class ConnectionManExceptionHandler implements Thread.UncaughtExceptionHandler {

    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof InterruptedException)
        {
            return;
        }
    }
}

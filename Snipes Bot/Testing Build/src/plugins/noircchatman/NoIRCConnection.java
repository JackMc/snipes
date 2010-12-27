package plugins.noircchatman;

import java.io.IOException;

public interface NoIRCConnection {
    public String recv();
    public void send(String s);
    public String getHost();
    public void close();
    boolean isOpen();
}

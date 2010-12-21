package plugins.noircchatman;

import java.io.IOException;

public interface NoIRCConnection {
    String recv() throws IOException;
    void send(String s) throws IOException;
}

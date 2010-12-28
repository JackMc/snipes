package plugins.noircchatman;

public interface NoIRCConnection {
    public String recv();
    public void send(String s);
    public String getHost();
    public void close();
    boolean isOpen();
}

package plugins.bouncer;

public interface PseudoClient
{
	public String getNick();

	public void performCommand(String command, BouncerConnection bc);

	public boolean isLineTo(String line, BouncerConnection bc);
}

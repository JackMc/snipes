package plugins.irchook;

public interface Hook
{
	public void line(String line);

	public boolean init();

	public void fini();
}

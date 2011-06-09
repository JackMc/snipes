package org.ossnipes.snipes.bot;

public class TestCommand implements Command
{

	public TestCommand(SnipesBot sb)
	{
		this._sb = sb;
	}

	@Override
	public String getCommand()
	{
		return "!test";
	}

	@Override
	public int getNumberOfArgs()
	{
		return 2;
	}

	@Override
	public void handleCommand(String command, String from, String[] spaceSplit)
	{
		this._sb.sendPrivMsg(from, command);
	}

	private final SnipesBot _sb;

}

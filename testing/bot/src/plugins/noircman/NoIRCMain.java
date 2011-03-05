package plugins.noircman;

public class NoIRCMain
{
	public NoIRCMain(NoIRCModule m)
	{
		if (m == null)
		{
			throw new IllegalArgumentException("m cannot be null.");
		}

		this._m = m;

		new NoIRCListener(this._m);
	}

	private final NoIRCModule _m;
}

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

		NoIRCListener noircl = new NoIRCListener(this._m);

		this._m.addEventListener(noircl);
	}

	NoIRCModule _m;
}

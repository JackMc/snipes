package plugins.irchook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogHook implements Hook
{

	@Override
	public void line(String line)
	{
		this._file.println(line);
	}

	@Override
	public boolean init()
	{
		try
		{
			this._file = new PrintStream(new FileOutputStream("snipes.log",
					true));
			Calendar cal = Calendar.getInstance();
			// If the file is empty, don't print a separator newline.
			if (new File("snipes.log").length() != 0)
			{
				this._file.println();
			}
			this._file.println("----Session: Date: "
					+ new SimpleDateFormat("dd/mm/yyyy").format(cal.getTime())
					+ ", Time: "
					+ new SimpleDateFormat("HH:MM:SS").format(cal.getTime())
					+ "----");
			return true;
		} catch (FileNotFoundException e)
		{
			System.err.println("Unable to initialise logging hook.");
		}
		return false;
	}

	@Override
	public void fini()
	{
		this._file.close();
	}

	private PrintStream _file;

}

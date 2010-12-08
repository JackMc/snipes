/*package org.ossnipes.snipes.net;

import java.util.logging.Level;

import org.ossnipes.snipes.utils.Log;

public class ConsoleConnection implements Connection {
	public ConsoleConnectionListener(int i) throws IOException {
		serverSock = new ServerSocket((i>0) ? i : 1337);
	}
	@Override
	public void init(int port) {
		if (port < 100)
		{
			Log.log("Console connection: WARNING: Port " + port + " may take root/admin priv. to bind to, trying anyways.", Level.WARNING);
		}
		
	}

}*/

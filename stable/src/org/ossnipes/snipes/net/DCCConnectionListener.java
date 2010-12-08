/*package org.ossnipes.snipes.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DCCConnectionListener implements ConnectionListener {

	ServerSocket serverSock;
	ArrayList<Socket> socks = new ArrayList<Socket>();
	
	public DCCConnectionListener(int i) throws IOException {
		serverSock = new ServerSocket((i>0) ? i : 1337);
	}

	@Override
	public boolean accept() {
		new Thread()
		{
			
		};
		return false;
	}

	@Override
	public String recv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean send(String msg) {
		// TODO Auto-generated method stub
		return false;
	}

}*/

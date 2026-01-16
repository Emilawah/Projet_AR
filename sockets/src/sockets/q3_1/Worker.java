package sockets.q3_1;

import java.net.Socket;

public class Worker implements Runnable{

	private Socket soc;
	
	public Worker(Socket socket) {
		this.soc = socket;
	}
	
	@Override
	public void run() {
		
	}

}

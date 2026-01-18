package sockets.q3_1;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Worker implements Runnable{

	private Socket soc;
	
	public Worker(Socket socket) {
		this.soc = socket;
	}
	
	@Override
	public void run() {
		try {
			InputStream is = soc.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

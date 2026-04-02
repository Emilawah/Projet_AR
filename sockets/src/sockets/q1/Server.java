package sockets.q1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Server {

	static int port = 4320;
	static int backlog = 3;
	static String name = "";

	public static void main(String[] args) throws IOException {

		ServerSocket listensoc = new ServerSocket(port, backlog);

		while (true) {
			Socket soc = listensoc.accept();

			InputStream is = soc.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			name = dis.readUTF();

			OutputStream os = soc.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF("Hello < " + name + " >");

		}
	}

}

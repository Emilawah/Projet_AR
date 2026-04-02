package sockets.q1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Client {

	static String serverHost = "localhost";
	static int serverPort = 4320;
	static String response = "";

	public static void main(String[] args) throws IOException {

		Socket soc = new Socket(serverHost, serverPort);

		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeUTF("Emilio");

		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		response = dis.readUTF();
		System.out.println(response);
		soc.close();

	}
}

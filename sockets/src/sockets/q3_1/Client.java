package sockets.q3_1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Client {

	static String serverHost = "localhost";
	static int serverPort = 4320;
	static String response = "";
	static String filename = "style.css";
	static String status;
	static byte[] buffer;

	public static void main(String[] args) throws IOException {

		Socket soc = new Socket(serverHost, serverPort);

		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);

		dos.writeUTF(filename.toString());

		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);

		buffer = new byte[1000];
		status = dis.readUTF();
		System.out.println(status);
		
		if (status.equals("File found !")) {

			FileOutputStream fos = new FileOutputStream(new File(filename));
			
			int nb;
			nb = dis.read(buffer);
			
			while (nb != -1) {
				fos.write(buffer, 0, nb);
				nb = dis.read(buffer);
			}
			
			fos.close();
			System.out.println("Download finished");
		}

		soc.close();

	}
}

package sockets.q2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Server {

	static int port = 4320;
	static int backlog = 3;
	static String filename = "";
	static String path = "files/";
	static File file;
	static byte[] buffer;

	public static void main(String[] args) throws IOException {

		ServerSocket listensoc = new ServerSocket(port, backlog);

		while (true) {
			Socket soc = listensoc.accept();

			InputStream is = soc.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			filename = dis.readUTF();
			file = new File(path + filename);
			buffer = new byte[1000];

			OutputStream os = soc.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);

			if (file.exists()) {

				dos.writeUTF("File found !");

				FileInputStream fis = new FileInputStream(file);
				int nb;
				nb = fis.read(buffer);

				while (nb != -1) {
					dos.write(buffer, 0, nb);
					nb = fis.read(buffer);
				}

				dos.flush();
				fis.close();
			} else {

				dos.writeUTF("File \"" + filename + "\" not found !");
			}
			soc.close();
		}
	}

}

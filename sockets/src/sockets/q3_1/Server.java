package sockets.q3_1;

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
			Thread worker = new Thread(new Worker(soc));
			worker.start();
		}
	}

}

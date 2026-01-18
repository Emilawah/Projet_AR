package sockets.q3;

import java.io.*;
import java.net.*;

public class MultiThreadedServer {

	public static void main(String[] args) throws IOException {
		ServerSocket listenSoc = new ServerSocket(4320);
		System.out.println("Serveur en attente...");

		while (true) {
			Socket soc = listenSoc.accept();
			System.out.println("Nouveau client connecté. Création d'un thread...");
			new Worker(soc).start();
		}
	}

	static class Worker extends Thread {
    	private Socket listenSoc;

        Worker(Socket soc) {
            this.listenSoc = soc;
        }
        
    	public void run() {
    		try (
    			DataInputStream dis = new DataInputStream(listenSoc.getInputStream());
                DataOutputStream dos = new DataOutputStream(listenSoc.getOutputStream())) {

                String fileName = dis.readUTF();
                   File file = new File("server_files/" + fileName);

                   if (file.exists()) {
                       try (FileInputStream fis = new FileInputStream(file)) {
                           byte[] buffer = new byte[1000];
                           int nb;
                           while ((nb = fis.read(buffer)) != -1) {
                               dos.write(buffer, 0, nb);
                           }
                       }
                   }
    		} catch (IOException e) {
                System.err.println("Erreur avec un client : " + e.getMessage());
    		} finally {
                try {
                    listenSoc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    	}
	}
}


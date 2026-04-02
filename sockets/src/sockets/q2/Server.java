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
    public static void main(String[] args) throws IOException {
        ServerSocket listenSoc = new ServerSocket(4320);
        System.out.println("Serveur en attente...");

        while (true) {
            try (Socket soc = listenSoc.accept();
                 DataInputStream dis = new DataInputStream(soc.getInputStream());
                 DataOutputStream dos = new DataOutputStream(soc.getOutputStream())) {

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
            }
            listenSoc.close();
        }
    }
}

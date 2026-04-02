package sockets.q2;

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

    public static void main(String[] args) throws IOException {
        String fileName = "index.html";
        
        try (Socket soc = new Socket(serverHost, serverPort);
             DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
             DataInputStream dis = new DataInputStream(soc.getInputStream())) {
            
            dos.writeUTF(fileName);
            File file = new File("client_files/" + fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1000];
                int nb;
                while ((nb = dis.read(buffer)) != -1) {
                    fos.write(buffer, 0, nb);
                }
                System.out.println("File downloaded !");
            }
        }
    }
}

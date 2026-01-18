package sockets.q3;

import java.io.*;
import java.net.Socket;

public class Client {
    static String serverHost = "localhost";
    static int serverPort = 4320;

    public static void main(String[] args) throws IOException {
        String fileName = "node.md";
        
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
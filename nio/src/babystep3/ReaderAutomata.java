package babystep3;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ReaderAutomata {
    public enum State { READING_LENGTH, READING_MSG };
    private State state = State.READING_LENGTH;
    private ByteBuffer bb = ByteBuffer.allocate(4);

    public byte[] handleRead(SocketChannel sc) throws IOException {
        int bytesRead = sc.read(bb);
        
        // Détection de la déconnexion
        if (bytesRead == -1) {
            throw new IOException("Client closed the connection (EOF)");
        }
        System.out.println("RA: Reading ... (" + bb.position() + "/4 bytes)");

        if (state == State.READING_LENGTH) {
            if (bb.hasRemaining()) {
                return null;
            }
            bb.rewind();
            int length = bb.getInt();
            System.out.println("RA: Length received : " + length);
            
            // Protection contre les messages invalides
            if (length <= 0 || length > 1024 * 1024) {
                throw new IOException("Invalid message length: " + length);
            }
            
            bb = ByteBuffer.allocate(length);
            state = State.READING_MSG;
        }

        if (state == State.READING_MSG) {
            if (bb.hasRemaining()) {
                return null;
            }
            
            // Message complet
            bb.rewind();
            byte[] msg = new byte[bb.limit()];
            bb.get(msg);
            
            // Réinitialisation pour le prochain message
            state = State.READING_LENGTH;
            bb = ByteBuffer.allocate(4);
            return msg;
        }
        return null;
    }
}
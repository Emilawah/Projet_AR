package babystep2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Automata {
    private WriterAutomata wa;
    private ReaderAutomata ra;
    private SocketChannel sc;
    
    public Automata(SocketChannel sc) {
        this.wa = new WriterAutomata();
        this.ra = new ReaderAutomata();
        this.sc = sc;
    }
    
    public byte[] handleRead(SelectionKey sk) throws IOException {
        return ra.handleRead(sc);
    }
    
    public void handleWrite(SelectionKey sk) throws IOException {
        wa.handleWrite(sk);
    }
    
    public void send(SelectionKey sk, byte[] data) {
        wa.sendMsg(sk, data);
    }
}
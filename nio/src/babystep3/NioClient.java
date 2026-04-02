package babystep3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Iterator;

public class NioClient {
    private SocketChannel sc;
    private Selector selector;
    private byte[] first;
    private byte[] digest;
    private int nloops = 0;

    public NioClient(String serverName, int port, byte[] payload) throws IOException {
        this.first = payload;
        selector = SelectorProvider.provider().openSelector();
        sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_CONNECT);
        sc.connect(new InetSocketAddress(InetAddress.getByName(serverName), port));
    }

    public void loop() throws IOException {
        System.out.println("NioClient running");
        while (true) {
            selector.select();
            Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                selectedKeys.remove();
                if (!key.isValid()) continue;

                if (key.isConnectable()) handleConnect(key);
                if (key.isReadable()) handleRead(key);
                if (key.isWritable()) handleWrite(key);
            }
        }
    }

    private void handleConnect(SelectionKey key) throws IOException {
        sc.finishConnect();
        Automata automata = new Automata(sc);
        key.attach(automata);
        key.interestOps(SelectionKey.OP_READ);
        
        digest = md5(first);
        automata.send(key, first);
    }

    private void handleRead(SelectionKey key) throws IOException {
        Automata automata = (Automata) key.attachment();
        try {
            byte[] data = automata.handleRead(key);
            if (data != null) {
                if (!md5check(digest, md5(data))) {
                    System.err.println("Checksum Error!");
                    return;
                }
                String msg = new String(data, Charset.forName("UTF-8"));
                System.out.println("NioClient received msg[" + nloops + "]: " + msg);
                
                if (nloops++ < 500) {
                    automata.send(key, data);
                } else {
                    System.out.println("Test finished successfully.");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.exit(0);
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        Automata automata = (Automata) key.attachment();
        automata.handleWrite(key);
    }

    // --- Méthodes MD5 fournies ---
    public static byte[] md5(byte[] bytes) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytes);
        } catch (Exception ex) { throw new IOException(ex); }
    }

    public static boolean md5check(byte[] d1, byte[] d2) {
        if (d1.length != d2.length) return false;
        for (int i = 0; i < d1.length; i++) if (d1[i] != d2[i]) return false;
        return true;
    }

    public static void main(String args[]) throws IOException {
        String msg = "Hello There...";
        byte[] bytes = msg.getBytes(Charset.forName("UTF-8"));
        new NioClient("localhost", 8888, bytes).loop();
    }
}
package babystep3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NioServer {
    public static final int DEFAULT_SERVER_PORT = 8888;
    private ServerSocketChannel ssc;
    private Selector selector;

    public NioServer(int port) throws IOException {
        selector = SelectorProvider.provider().openSelector();
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        InetAddress hostAddress = InetAddress.getByName("localhost");
        ssc.socket().bind(new InetSocketAddress(hostAddress, port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void loop() throws IOException {
        System.out.println("NioServer running");
        while (true) {
            selector.select();
            Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = (SelectionKey) selectedKeys.next();
                selectedKeys.remove();

                if (!key.isValid()) continue;

                if (key.isAcceptable()) 
                    handleAccept(key);

                if (key.isValid() && key.isReadable()) 
                    handleRead(key);

                if (key.isValid() && key.isWritable()) 
                    handleWrite(key);
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        Automata automata = new Automata(sc);
        sc.register(selector, SelectionKey.OP_READ, automata);
        System.out.println("NioServer: New connection from " + sc.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) throws IOException {
        Automata automata = (Automata) key.attachment();
        try {
            byte[] data = automata.handleRead(key);
            if (data != null) {
                String msg = new String(data, Charset.forName("UTF-8"));
                System.out.println("NioServer received: " + msg);
                automata.send(key, data);
            }
        } catch (IOException e) {
            System.err.println("NioServer: Client disconnected.");
            key.cancel();
            key.channel().close();
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        Automata automata = (Automata) key.attachment();
        try {
            automata.handleWrite(key);
        } catch (IOException e) {
            key.cancel();
            key.channel().close();
        }
    }

    public static void main(String args[]) throws IOException {
        int serverPort = DEFAULT_SERVER_PORT;
        if (args.length > 1 && args[0].equals("-p")) 
            serverPort = Integer.parseInt(args[1]);
        new NioServer(serverPort).loop();
    }
}
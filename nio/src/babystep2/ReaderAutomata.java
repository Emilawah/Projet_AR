package babystep2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ReaderAutomata {

	enum State {
		READING_LENGTH, READING_MSG
	};

	private State state = State.READING_LENGTH;
	private ByteBuffer bb = ByteBuffer.allocate(4);
	private byte[] data;

	public byte[] handleRead(SocketChannel sc) throws IOException {

		if (state == State.READING_LENGTH) {

			// on alloue le buffer à 4 octets pour lire la taille du message
			sc.read(bb);
			System.out.println("RA : Reading length ("+bb.position() + "/4 bytes)");

			if (bb.hasRemaining()) {
				return null;
			}

			bb.rewind();
			// if the four bytes composing the length have been read
			// allocate a buffer to read the msg
			int length = bb.getInt();
			bb = ByteBuffer.allocate(length);
			System.out.println("RA : Length received : " + length);
			state = State.READING_MSG;

		}

		if (state == State.READING_MSG) {
			sc.read(bb);
			if (bb.hasRemaining()) {
				return null;
			}

			// on set la position à 0 pour lire les octets au debut.
			bb.rewind();
			data = new byte[bb.limit()];
			bb.get(data);
			state = State.READING_LENGTH;
			bb = ByteBuffer.allocate(4);
			return data;

		}
		return null;
	}

}

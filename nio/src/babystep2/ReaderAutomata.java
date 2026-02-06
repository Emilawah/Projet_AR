package babystep2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ReaderAutomata {

	enum State {
		READING_LENGTH, READING_MSG
	};

	private State state = State.READING_LENGTH;
	private ByteBuffer bb;
	
	
	public void handleRead(SocketChannel sc) throws IOException {

		if (state == State.READING_LENGTH) {
			
			int length = bb.getInt();
			// if the four bytes composing the length have been read
			state = State.READING_MSG;
		}
		else if (state == State.READING_MSG) {
			sc.read(bb);
			
			//if all bytes composing the msg have been read
			if(bb.remaining() == 0) {
				
				state = State.READING_LENGTH;
			}
		}
	}

}

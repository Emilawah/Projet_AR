package babystep2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriterAutomata {

	enum State {
		WRITING_LENGTH, WRITING_MSG, WRITING_IDLE
	};
	
	private ByteBuffer bb;
	private State state = State.WRITING_IDLE;
	
	void sendMsg(byte[] msg){
		
	}
	
	void handleWrite(SocketChannel sc) throws IOException {
		sc.write(bb);
	}
}

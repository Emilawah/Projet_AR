package babystep2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class WriterAutomata {

	enum State {
		WRITING_LENGTH, WRITING_MSG, WRITING_IDLE
	};

	private State state = State.WRITING_IDLE;
	private LinkedList<byte[]> pendingMsgs = new LinkedList<>();
	private ByteBuffer bb = ByteBuffer.allocate(4);
	private ByteBuffer bbMsg;

	void sendMsg(SelectionKey sk, byte[] msg) {
		pendingMsgs.add(msg);
		sk.interestOps(SelectionKey.OP_WRITE);
	}

	void handleWrite(SelectionKey sk) throws IOException {

		SocketChannel sc = (SocketChannel) sk.channel();

		if (state == State.WRITING_IDLE && !pendingMsgs.isEmpty()) {

			byte[] msgtoWrite = pendingMsgs.removeFirst();
			bb.clear();
			bb.putInt(msgtoWrite.length);
			bb.rewind();

			bbMsg = ByteBuffer.wrap(msgtoWrite);
			state = State.WRITING_LENGTH;
		}
		if (state == State.WRITING_LENGTH) {
			sc.write(bb);
			if (bb.hasRemaining()) {
				return;
			}
			state = State.WRITING_MSG;

		}
		if (state == State.WRITING_MSG) {
			sc.write(bbMsg);
			if (bbMsg.hasRemaining()) {
				return;
			}
			state = State.WRITING_IDLE;
			bbMsg = null;

			if (pendingMsgs.isEmpty()) {
				sk.interestOps(SelectionKey.OP_READ);
			}
		}
	}
}

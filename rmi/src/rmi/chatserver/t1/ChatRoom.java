package rmi.chatserver.t1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

	private String idChat;
	private List<IParticipant> participants;

	protected ChatRoom(String name) throws RemoteException {
		this.idChat = name;
		this.participants = new ArrayList<>();
	}

	public String name() throws RemoteException {
		return idChat;
	}

	public synchronized void connect(IParticipant p) throws RemoteException {
		participants.add(p);
		System.out.println(p.name() + " a rejoint la session");
	}

	public synchronized void leave(IParticipant p) throws RemoteException {
		participants.remove(p);
		System.out.println(p.name() + " a quitté la session");
	}

	public synchronized String[] who() throws RemoteException {
		String[] names = new String[participants.size()];
		for(int i = 0; i < participants.size() ; i++) {
			names[i] = participants.get(i).name();
		}
		return names;
		
	}

	public synchronized void send(IParticipant p, String msg) throws RemoteException {
		String senderName = p.name();
		for (IParticipant participant : participants) {
			try {
				participant.receive(senderName, msg);
			} catch (RemoteException e) {
				System.err.println("Impossible de rejoindre la session");
			}
		}

	}

}

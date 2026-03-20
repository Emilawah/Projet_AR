package rmi.chatserver.t1;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public static void main(String args[]) throws RemoteException, AlreadyBoundException {
		
		
		try {
			IChatRoom room = new ChatRoom("Géneral chat");
			Registry registry = LocateRegistry.createRegistry(9999);
			registry.bind("ChatRoom", room);
			
			System.out.println("Serveur "+ room.name() + " prêt");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

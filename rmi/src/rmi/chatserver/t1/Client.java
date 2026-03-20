package rmi.chatserver.t1;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmi.babystep.IPrinter;

public class Client {

	public static void main(String args[]) throws RemoteException, NotBoundException {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Entrez votre nom : ");
		String name = sc.nextLine();
		
		Registry reg = LocateRegistry.getRegistry("localhost", 9999);
		IChatRoom room = (IChatRoom) reg.lookup("ChatRoom");
		
		IParticipant p = new Participant(name);
		
		room.connect(p);
		System.out.println("Connecté à " + room.name());
		System.out.println("Tapez who' pour voir les membres ou quit' pour partir.");
		
		while(true) {
			System.out.println(">");
			String input = sc.nextLine();
			
			if(input.equalsIgnoreCase("quit")){
				room.leave(p);
				break;
			} else if(input.equalsIgnoreCase("who")) {
				for(String part : room.who()) {
					System.out.println("- " + part);
				}
			}else {
				room.send(p, input);
			}
		}
		System.exit(0);
	}
}

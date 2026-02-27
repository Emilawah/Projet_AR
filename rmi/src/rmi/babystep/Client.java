package rmi.babystep;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

	
	public static void main(String args[]) throws RemoteException, NotBoundException {
		
		Registry reg = LocateRegistry.getRegistry("localhost", 9999);
		IPrinter printer = (IPrinter) reg.lookup("LinePrinter");
		printer.print("hello");
	}
	
	
}

package rmi.babystep;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public static void main(String args[]) throws RemoteException, AlreadyBoundException {
		IPrinter printer =  new Printer();
		Registry registry = LocateRegistry.createRegistry(9999);
		registry.bind("LinePrinter", printer);
	}

}

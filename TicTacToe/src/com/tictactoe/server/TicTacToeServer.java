package com.tictactoe.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class TicTacToeServer {
	public static void main(String[] args) throws MalformedURLException, RemoteException, AlreadyBoundException {
		LocateRegistry.createRegistry(1099); 
		Naming.rebind("tictacToeServer", new ServerImpl());
		System.out.println("Started Server");
	}
}

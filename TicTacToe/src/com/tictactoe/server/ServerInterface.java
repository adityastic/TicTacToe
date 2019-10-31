package com.tictactoe.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.tictactoe.GameClickInfo;
import com.tictactoe.client.ClientInferface;

public interface ServerInterface extends Remote{
	void clickedLocation(GameClickInfo clickInfo) throws RemoteException;
	void registerClient(ClientInferface client) throws RemoteException;
}

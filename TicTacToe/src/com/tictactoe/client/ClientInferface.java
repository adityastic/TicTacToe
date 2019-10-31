package com.tictactoe.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.tictactoe.ClientMessage;
import com.tictactoe.ScoreBoard;

public interface ClientInferface extends Remote{
	void receiveState(int[][] state) throws RemoteException;
	boolean checkAlive() throws RemoteException;
	void showMessage(ClientMessage message) throws RemoteException;
	void getScores(ScoreBoard scores) throws RemoteException;
}

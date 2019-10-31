package com.tictactoe.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.tictactoe.ClientMessage;
import com.tictactoe.GameClickInfo;
import com.tictactoe.ScoreBoard;
import com.tictactoe.server.ServerInterface;

public class ClientImpl extends UnicastRemoteObject implements ClientInferface{

	private static final long serialVersionUID = 1L;

	private ServerInterface serverInst;
	private int playerNum;
	private OnMessageListener listener;
	protected boolean currentState = false;

	public ClientImpl(ServerInterface serverInst, OnMessageListener listener) throws RemoteException {
		this.serverInst = serverInst;
		this.listener = listener;
		this.serverInst.registerClient(this);
	}

	@Override
	public boolean checkAlive() throws RemoteException {
		return true;
	}

	@Override
	public void receiveState(int[][] state) throws RemoteException {
		listener.receiveState(state);
	}

	@Override
	public void showMessage(ClientMessage message) throws RemoteException {
		switch (message) {
		case ALREADY_PLAYERS:
			listener.showMessage("Already Players Present");
			break;
		case READY_PLAYER_1:
			playerNum = 1;
			listener.showMessage("READY PLAYER 1");
			break;
		case READY_PLAYER_2:
			listener.showMessage("READY PLAYER 2");
			playerNum = 2;
			break;
		case WAITING_PLAYER_2:
			listener.showMessage("Waiting for Player 2");
			break;
		case CHANCE_FOR_P1:
			if(playerNum == 1) {
				currentState = true;
				listener.showMessage("Your Turn");
			}else {
				currentState = false;
				listener.showMessage("Chance for Player 1");
			}
			break;
		case CHANCE_FOR_P2:
			if(playerNum == 2) {
				currentState = true;
				listener.showMessage("Your Turn");
			}else {
				currentState = false;
				listener.showMessage("Chance for Player 2");
			}
			break;
		case R3:
			listener.showMessage("Restarting Game in 3...");
			currentState = false;
			break;
		case R2:
			listener.showMessage("Restarting Game in 2...");
			currentState = false;
			break;
		case R1:
			listener.showMessage("Restarting Game in 1...");
			currentState = false;
			break;
		case WON1:
			listener.showMessage("Player 1 Won the Game");
			currentState = false;
			break;
		case WON2:
			listener.showMessage("Player 2 Won the Game");
			currentState = false;
			break;
		default:
			break;
		}
	}

	public String clickedLocation(int i, int j) throws RemoteException {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(currentState)
					try {
						serverInst.clickedLocation(new GameClickInfo(playerNum, i, j));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}).start();
		return ((playerNum == 1) ? "O" :(playerNum == 2)? "X": "-");
	}

	@Override
	public void getScores(ScoreBoard scores) throws RemoteException {
		listener.getScores(scores);
	}
}

interface OnMessageListener{
	void receiveState(int[][] state);
	void showMessage(String msg);
	void getScores(ScoreBoard scores);
}

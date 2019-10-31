package com.tictactoe.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.Timer;

import com.tictactoe.ClientMessage;
import com.tictactoe.GameClickInfo;
import com.tictactoe.ScoreBoard;
import com.tictactoe.TicTacToe;
import com.tictactoe.client.ClientInferface;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface{
	private static final long serialVersionUID = 1L;
	private ArrayList<ClientInferface> clients;
	private TicTacToe game;

	protected ServerImpl() throws RemoteException {
		this.clients = new ArrayList<>();

		game = new TicTacToe(new ScoreBoard());

		Timer timer = new Timer(4000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					checkLostClients();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		timer.setRepeats(true);
		timer.start();
	}

	private void performOperation(GameClickInfo clickInfo) throws RemoteException {
		if(game.checkClickValid(clickInfo))
			return;

		if (game.isCurrentPlayer(clickInfo)) {
			game.clickAtPosition(clickInfo);
			sendChangeBroadcast(true);
		}
		broadcastBoard();

		if(ifWonGame())
			return;
		ifEndGame();
	}

	private void ifEndGame() {
		if(game.ifGameEnded()) {
			Timer timer = new Timer(1000, new ActionListener() {
				private int counter = 1;
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						switch(counter++) {
						case 1:
							clients.get(0).showMessage(ClientMessage.R3);
							clients.get(1).showMessage(ClientMessage.R3);
							break;
						case 2:
							clients.get(0).showMessage(ClientMessage.R2);
							clients.get(1).showMessage(ClientMessage.R2);
							break;
						case 3:
							clients.get(0).showMessage(ClientMessage.R1);
							clients.get(1).showMessage(ClientMessage.R1);
							break;
						}
						if (counter >= 5) {
							resetBoard();
							broadcastBoard();
							sendChangeBroadcast(false);
							((Timer)e.getSource()).stop();
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			});
			timer.start();
		}
	}

	private void resetBoard() throws RemoteException {
		game.resetBoard();
	}

	private void resetScore() {
		game.resetScore();
	}

	private boolean ifWonGame() throws RemoteException {
		int winState = 0;
		if(game.checkGamefor(1)) {
			winState = 1;
			game.increasePlayer1Score();
		}

		if(game.checkGamefor(2)) {
			winState = 2;
			game.increasePlayer2Score();
		}

		if(winState > 0) {
			switch(winState) {
			case 1:
				clients.get(0).showMessage(ClientMessage.WON1);
				clients.get(1).showMessage(ClientMessage.WON1);
				break;
			case 2:
				clients.get(0).showMessage(ClientMessage.WON2);
				clients.get(1).showMessage(ClientMessage.WON2);
				break;
			}

			Timer timer = new Timer(1000, new ActionListener() {
				private int counter = 1;
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						switch(counter++) {
						case 3:
							clients.get(0).showMessage(ClientMessage.R3);
							clients.get(1).showMessage(ClientMessage.R3);
							break;
						case 4:
							clients.get(0).showMessage(ClientMessage.R2);
							clients.get(1).showMessage(ClientMessage.R2);
							break;
						case 5:
							clients.get(0).showMessage(ClientMessage.R1);
							clients.get(1).showMessage(ClientMessage.R1);
							break;
						}
						if (counter >= 7) {
							resetBoard();
							broadcastBoard();
							sendChangeBroadcast(false);
							((Timer)e.getSource()).stop();
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			});
			timer.start();

			broadCastScores();
			return true;
		}
		return false;
	}

	private void broadCastScores() throws RemoteException {
		clients.get(0).getScores(game.getScore());
		clients.get(1).getScores(game.getScore());
	}

	private void sendChangeBroadcast(boolean ifChange) throws RemoteException {
		if(ifChange)
			game.changeCurrentChance();

		if(game.getCurrentChance() == 1) {
			clients.get(0).showMessage(ClientMessage.CHANCE_FOR_P1);
			clients.get(1).showMessage(ClientMessage.CHANCE_FOR_P1);
		} else {
			clients.get(0).showMessage(ClientMessage.CHANCE_FOR_P2);
			clients.get(1).showMessage(ClientMessage.CHANCE_FOR_P2);
		}
	}

	private void broadcastBoard() throws RemoteException {
		for (ClientInferface cl : clients) {
			cl.receiveState(game.getBoard());
		}
	}

	protected void checkLostClients() throws RemoteException {
		ArrayList<ClientInferface> toDelete = new ArrayList<>();
		for (ClientInferface client : clients) {
			try {
				client.checkAlive();
			} catch (RemoteException e) {
				toDelete.add(client);
			}
		}
		if(toDelete.size() > 0) {
			System.out.println("Removed " + toDelete.size() + " persons");
			clients.removeAll(toDelete);

			if(clients.size()>0) {
				resetBoard();
				resetScore();
				clients.get(0).getScores(game.getScore());
				clients.get(0).showMessage(ClientMessage.WAITING_PLAYER_2);
			}
		}
	}

	@Override
	public void registerClient(ClientInferface client) throws RemoteException {
		if(clients.size() < 2) {
			clients.add(client);
			broadcastBoard();

			if(clients.size() < 2) {
				client.showMessage(ClientMessage.READY_PLAYER_1);
				client.showMessage(ClientMessage.WAITING_PLAYER_2);
			} else {
				client.showMessage(ClientMessage.READY_PLAYER_2);
				clients.get(0).showMessage(ClientMessage.READY_PLAYER_1);
				client.showMessage(ClientMessage.CHANCE_FOR_P1);
				clients.get(0).showMessage(ClientMessage.CHANCE_FOR_P1);
				broadCastScores();
			}
		}
		else
			client.showMessage(ClientMessage.ALREADY_PLAYERS);
	}

	@Override
	public void clickedLocation(GameClickInfo clickInfo) throws RemoteException {
		performOperation(clickInfo);
	}
}

package com.tictactoe;

import java.io.Serializable;

public class GameClickInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int playerNumber, i, j;

	public GameClickInfo(int playerNumber, int i, int j) {
		super();
		this.playerNumber = playerNumber;
		this.i = i;
		this.j = j;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	@Override
	public String toString() {
		return "GameClickInfo [playerNumber=" + playerNumber + ", i=" + i + ", j=" + j + "]";
	}
}

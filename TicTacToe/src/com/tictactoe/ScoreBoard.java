package com.tictactoe;

import java.io.Serializable;

public class ScoreBoard implements Serializable{
	private static final long serialVersionUID = 1L;
	private int score1, score2;

	public ScoreBoard() {
		this.score1 = 0;
		this.score2 = 0;
	}

	public void increasePlayer1Score() {
		this.score1++;
	}

	public void increasePlayer2Score() {
		this.score2++;
	}

	public int getScore1() {
		return this.score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}

	public void reset() {
		this.score1 = 0;
		this.score2 = 0;
	}
}

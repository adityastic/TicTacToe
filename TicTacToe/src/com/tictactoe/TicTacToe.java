package com.tictactoe;

public class TicTacToe {
	int[][] gameBoard = new int[3][3];
	int currentChance = 1;
	ScoreBoard score;

	public TicTacToe(ScoreBoard score) {
		this.score = score;
	}

	public boolean checkClickValid(GameClickInfo clickInfo) {
		return this.gameBoard[clickInfo.getI()][clickInfo.getJ()] > 0;
	}

	public boolean isCurrentPlayer(GameClickInfo clickInfo) {
		return this.currentChance == clickInfo.getPlayerNumber();
	}

	public void clickAtPosition(GameClickInfo clickInfo) {
		this.gameBoard[clickInfo.getI()][clickInfo.getJ()] = clickInfo.getPlayerNumber();
	}

	public void resetBoard() {
		this.gameBoard = new int[3][3];
	}

	public boolean ifGameEnded() {
		for (int i = 0; i < this.gameBoard.length; i++) {
			for (int j = 0; j < this.gameBoard[i].length; j++) {
				if(this.gameBoard[i][j] == 0)
					return false;
			}
		}
		return true;
	}

	public boolean checkGamefor(int player) {
		return (
				(this.gameBoard[0][0] == player && this.gameBoard[0][1] == player && this.gameBoard[0][2] == player) || 
				(this.gameBoard[1][0] == player && this.gameBoard[1][1] == player && this.gameBoard[1][2] == player) || 
				(this.gameBoard[2][0] == player && this.gameBoard[2][1] == player && this.gameBoard[2][2] == player) || 
				(this.gameBoard[0][0] == player && this.gameBoard[1][0] == player && this.gameBoard[2][0] == player) || 
				(this.gameBoard[0][1] == player && this.gameBoard[1][1] == player && this.gameBoard[2][1] == player) || 
				(this.gameBoard[0][2] == player && this.gameBoard[1][2] == player && this.gameBoard[2][2] == player) || 
				(this.gameBoard[0][0] == player && this.gameBoard[1][1] == player && this.gameBoard[2][2] == player) || 
				(this.gameBoard[0][2] == player && this.gameBoard[1][1] == player && this.gameBoard[2][0] == player) 
				);
	}

	public void increasePlayer1Score() {
		this.score.increasePlayer1Score();
	}
	
	public void increasePlayer2Score() {
		this.score.increasePlayer2Score();
	}

	public ScoreBoard getScore() {
		return this.score;
	}

	public int[][] getBoard() {
		return this.gameBoard;
	}

	public void changeCurrentChance() {
		this.currentChance = (this.currentChance == 1)? 2: 1;
	}

	public int getCurrentChance() {
		return this.currentChance;
	}

	public void resetScore() {
		score.reset();
	}
}

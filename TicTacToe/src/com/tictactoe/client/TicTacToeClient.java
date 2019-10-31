package com.tictactoe.client;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.tictactoe.ScoreBoard;
import com.tictactoe.server.ServerInterface;

import java.awt.Font;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TicTacToeClient {

	private JFrame frame;
	private JPanel panel;
	private JButton buttons[] = new JButton[9]; 
	private JLabel lblPlayer;
	private JLabel lblPlayer_1;
	private JLabel points1;
	private JLabel points2;
	JLabel lblMessageInformation;

	private ClientImpl client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TicTacToeClient window = new TicTacToeClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TicTacToeClient() throws MalformedURLException, RemoteException, NotBoundException {
		initialize();
		initializeButtons();
		initializeClient();
	}

	private void initializeButtons() {
		int k = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[k] = new JButton();
				buttons[k].setText("-");
				buttons[k].setActionCommand(""+i+j);
				buttons[k].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							client.clickedLocation(
									e.getActionCommand().charAt(0) - '0',
									e.getActionCommand().charAt(1) - '0'
									);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				});

				panel.add(buttons[k++]);      
			}
		}
	}

	private void initializeClient() throws MalformedURLException, RemoteException, NotBoundException {
		client = new ClientImpl((ServerInterface) Naming.lookup("rmi:///tictacToeServer"), new OnMessageListener() {

			@Override
			public void showMessage(String msg) {
				lblMessageInformation.setText(msg);	
			}

			@Override
			public void receiveState(int[][] state) {
				int k = 0;
				for (int i = 0; i < state.length; i++) {
					for (int j = 0; j < state[i].length; j++) {
						buttons[k++].setText(""+ ((state[i][j] == 1) ? "O" :(state[i][j] == 2)? "X": "-"));
					}
				}
			}

			@Override
			public void getScores(ScoreBoard scores) {
				points1.setText(""+scores.getScore1());
				points2.setText(""+scores.getScore2());
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 355);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblTicTacToe = new JLabel("TIC TAC TOE");
		lblTicTacToe.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblTicTacToe.setHorizontalAlignment(SwingConstants.CENTER);
		lblTicTacToe.setBounds(153, 6, 122, 29);
		frame.getContentPane().add(lblTicTacToe);

		panel = new JPanel();
		panel.setBounds(98, 71, 252, 201);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(3,3));

		lblPlayer = new JLabel("Player 1");
		lblPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer.setBounds(16, 84, 61, 16);
		frame.getContentPane().add(lblPlayer);

		lblPlayer_1 = new JLabel("Player 2");
		lblPlayer_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer_1.setBounds(367, 84, 61, 16);
		frame.getContentPane().add(lblPlayer_1);

		points1 = new JLabel("0");
		points1.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		points1.setHorizontalAlignment(SwingConstants.CENTER);
		points1.setBounds(16, 142, 61, 75);
		frame.getContentPane().add(points1);

		points2 = new JLabel("0");
		points2.setHorizontalAlignment(SwingConstants.CENTER);
		points2.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		points2.setBounds(367, 142, 61, 75);
		frame.getContentPane().add(points2);

		JLabel lblMessage = new JLabel("Message: ");
		lblMessage.setBounds(83, 297, 71, 16);
		frame.getContentPane().add(lblMessage);

		lblMessageInformation = new JLabel("Message Information");
		lblMessageInformation.setBounds(153, 297, 202, 16);
		frame.getContentPane().add(lblMessageInformation);
	}
}

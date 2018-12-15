package com.gmail.kramarenko104.seabattle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gmail.kramarenko104.seabattle.controller.*;

public class GameBoard extends JFrame {

	private Font labelFont = new Font("Verdana", Font.ITALIC, 20);
	private Font titleFont = new Font("Verdana", Font.BOLD, 20);
	private Font buttonFont = new Font("Verdana", Font.BOLD, 23);
	private Font textFont = new Font("Verdana", Font.BOLD, 23);
	private Font boldTextFont = new Font("Verdana", Font.BOLD, 15);
	private Font numbersTextFont = new Font("Verdana", Font.BOLD, 17);
	private JLabel info = new JLabel();
	private JButton buttonExit = new JButton("EXIT");
	private JButton buttonRestart = new JButton("RESTART");
	private JPanel mainPanel = new JPanel();
	private String sInfoYourTurn = "Your turn...Press any square on computer's board to guess its boats location...";
	private String sInfoCompTurn = "Computer is playing ...";

	private JButton[][] compBoard;
	private JButton[][] personBoard;
	private Person person;
	private Computer comp;
	private int fieldSize;

	public GameBoard(int fieldSize) {
		super.setTitle("SEA BATTLE (v.1.0) by Julia Kramarenko");
		this.fieldSize = fieldSize;
		compBoard = new JButton[fieldSize][fieldSize];
		personBoard = new JButton[fieldSize][fieldSize];
	}

	public void init(Person person, Computer comp) {
		this.person = person;
		this.comp = comp;

		setLayout(new BorderLayout());
		info.setFont(titleFont);
		info.setForeground(Color.BLUE);
		info.setText(sInfoYourTurn);
		info.setHorizontalAlignment(0);
		add(info, BorderLayout.NORTH);

		mainPanel.setLayout(new GridLayout(1, 2, 20, 1));
		add(mainPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2, 1, 1));
		buttonRestart.setFont(buttonFont);
		buttonPanel.add(buttonRestart);

		buttonExit.setFont(buttonFont);
		buttonPanel.add(buttonExit);
		add(buttonPanel, BorderLayout.SOUTH);
		setSize(1350, 700);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		initListeners();

		boardCreation(person.getName(), Color.PINK);
		boardCreation(comp.getName(), Color.GREEN);
		comp.autoFillBoard();
		person.autoFillBoard();

		updateBoardCellsValues();

		setVisible(true);
	}

///////////////////////////////////////
	private void initListeners() {

		buttonRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				info.setFont(titleFont);
				info.setForeground(Color.BLUE);
				info.setText(sInfoYourTurn);

				comp.clearBoard();
				person.clearBoard();

				comp.autoFillBoard();
				person.autoFillBoard();

				updateBoardCellsValues();
			}
		});

		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	private void updateBoardCellsValues() {
		for (int i = 0; i < fieldSize; i++) {
			for (int j = 0; j < fieldSize; j++) {
				personBoard[i][j].setText(person.getText(i, j));
				personBoard[i][j].setBackground(person.getColor(i, j));
				if (comp.isShown(i, j)) {
					compBoard[i][j].setText(comp.getText(i, j));
					compBoard[i][j].setBackground(comp.getColor(i, j));
				}else {
					compBoard[i][j].setText("");
					compBoard[i][j].setBackground(Color.WHITE);
				}
			}
		}
	}

///////////////////////////////////////
	// empty boards creation
	private void boardCreation(String playerName, Color color) {
		JPanel oneGamerPanel = new JPanel();
		oneGamerPanel.setLayout(new BorderLayout());
		oneGamerPanel.setBackground(color);
		JLabel title = new JLabel(playerName + "'s board");
		title.setFont(labelFont);
		oneGamerPanel.add(title, BorderLayout.NORTH);

		char text = 0;
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new BorderLayout());

		// horizontal alphabetical Panel
		JPanel alphabetPanel = new JPanel();
		alphabetPanel.setBackground(Color.GRAY);
		alphabetPanel.setLayout(new GridLayout(1, fieldSize + 1, 1, 1));
		for (int i = 0; i < fieldSize + 1; i++) {
			switch (i) {
			case 1:
				text = '\u0410'; break;
			case 2:
				text = '\u0411'; break;
			case 3:
				text = '\u0412'; break;
			case 4:
				text = '\u0413'; break;
			case 5:
				text = '\u0414'; break;
			case 6:
				text = '\u0415'; break;
			case 7:
				text = '\u0416'; break;
			case 8:
				text = '\u0417'; break;
			case 9:
				text = '\u0418'; break;
			case 10:
				text = '\u041a'; break;
			default:
				text = '\u0020'; break;
			}
			JButton but = new JButton(Character.toString(text));
			but.setEnabled(false);
			but.setFont(boldTextFont);
			alphabetPanel.add(but);
		}
		subPanel.add(alphabetPanel, BorderLayout.NORTH);

		// vertical numeric Panel
		JPanel numbersPanel = new JPanel();
		numbersPanel.setBackground(Color.GRAY);
		numbersPanel.setLayout(new GridLayout(fieldSize, 1, 1, 1));
		for (int i = 1; i < fieldSize + 1; i++) {
			JButton but = new JButton(String.valueOf(i));
			but.setEnabled(false);
			but.setFont(numbersTextFont);
			numbersPanel.add(but);
		}
		subPanel.add(numbersPanel, BorderLayout.WEST);

		// battle Panel
		JPanel gridPanel = new JPanel();
		gridPanel.setBackground(Color.GRAY);
		gridPanel.setLayout(new GridLayout(fieldSize, fieldSize, 1, 1));
		comp.createCells();
		person.createCells();

		for (int i = 0; i < fieldSize; i++) {
			for (int j = 0; j < fieldSize; j++) {
				JButton but = new JButton();
				but.setFont(textFont);
				final int ii = i;
				final int jj = j;
				but.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Person press any cell
						comp.processShot(ii, jj);
						updateBoardCellsValues();
						checkWinner();
						
						// Computer tries to guess where person's boats are located
						info.setText(sInfoCompTurn);
						person.processShot();
						updateBoardCellsValues();
						checkWinner();
						info.setText(sInfoYourTurn);
					}
				});
				if (playerName.equals("Computer")) {
					compBoard[i][j] = but;
				} else {
					personBoard[i][j] = but;
					but.setEnabled(false);
				}
				gridPanel.add(but);
			}
		}
		subPanel.add(gridPanel, BorderLayout.CENTER);
		oneGamerPanel.add(subPanel, BorderLayout.CENTER);
		mainPanel.add(oneGamerPanel);
	}
	
	private void checkWinner() {
		if (person.getKilledBoatCount() == 10 || comp.getKilledBoatCount() == 10) {
			info.setFont(textFont);
			info.setForeground(Color.RED);
			if (person.getKilledBoatCount() == 10) {
				info.setText("Computer won! Game is over!");
			} else if (comp.getKilledBoatCount() == 10){
				info.setText("You won! Game is over!");
			}
		}
	}
}

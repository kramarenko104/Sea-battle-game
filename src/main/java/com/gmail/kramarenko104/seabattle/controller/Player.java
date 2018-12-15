package com.gmail.kramarenko104.seabattle.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import com.gmail.kramarenko104.seabattle.model.*;

public class Player {

	private String name;
	public static int FIELD_SIZE;
	protected Cell[][] cells;
	protected Boat[] boats;
	protected int boatsIndex;
	protected int killedBoats;

	public Player(String name) {
		this.name = name;
		cells = new Cell[FIELD_SIZE][FIELD_SIZE];
		boats = new Boat[10];
	}

	public static void setArrSize(int fieldSize) {
		FIELD_SIZE = fieldSize;
	}

	public String getName() {
		return name;
	}

	public Cell[][] getCells() {
		return cells;
	}

	public String getText(int i, int j) {
		return (cells[i][j].isInjured() ? "x" : (cells[i][j].isKilled() ? Character.toString('\u0436') : (cells[i][j].isPassed() ? "." : "")));
	}

	public boolean isShown(int i, int j) {
		return cells[i][j].isShown();
	}

	public int getKilledBoatCount() {
		return killedBoats;
	}

	public void createCells() {
		for (int i = 0; i < FIELD_SIZE; i++) {
			for (int j = 0; j < FIELD_SIZE; j++) {
				cells[i][j] = new Cell(true, false, Color.WHITE, State.NA);
			}
		}
	}

	public void clearBoard() {
		boatsIndex = 0;
		for (int i = 0; i < Player.FIELD_SIZE; i++) {
			for (int j = 0; j < Player.FIELD_SIZE; j++) {
				cells[i][j].setBoat(false);
				cells[i][j].setShown(false);
				cells[i][j].setColor(Color.WHITE);
				cells[i][j].setState(State.NA);
			}
		}
		for (int i = 0; i < 10; i++) {
			boats[i] = null;
		}
	}

	private void fillCell(Boat boat, int x, int y, Color color) {
		cells[x][y].setBoat(true);
		cells[x][y].setShown(false);
		cells[x][y].setColor(color);
		cells[x][y].setBoat(boat);
		cells[x][y].setState(State.NA);
	}

	public void autoFillBoard() {
		// one 4-decks boat
		createDeckBoat(4, 1);
		// two 3-decks boats
		createDeckBoat(3, 2);
		// three 2-decks boats
		createDeckBoat(2, 3);
		// four 1-deck boats
		createDeckBoat(1, 4);
	}

	protected void createDeckBoat(int deck, int count) {
		int currX;
		int currY;
		boolean correct = true;
		int boatsCount = 0;
		Color color = null;

		if (this.getName().equals("Computer")) {
			color = Color.WHITE;
		} else {
			switch (deck) {
			case 1:
				color = new Color(152, 251, 152);
				break;
			case 2:
				color = new Color(255, 255, 100);
				break;
			case 3:
				color = new Color(135, 206, 237);
				break;
			case 4:
				color = new Color(221, 160, 221);
				break;
			}
		}

		while (boatsCount < count) {
			// select randomly start point for boat
			do {
				currX = (int) (Math.random() * FIELD_SIZE);
				currY = (int) (Math.random() * FIELD_SIZE);
				if (deck < 4) {
					correct = isCorrectLocation(currX, currY);
				}
			} while (!correct);

			// randomly select what side (up, down, left, right) to build boat
			int randMoveChoise = (int) (Math.random() * 4);
			if (tryMove(currX, currY, deck, color, randMoveChoise)) {
				boatsCount++;
			}
		}
	}

	private boolean tryMove(int currX, int currY, int deck, Color color, int randMoveChoise) {
		boolean correct = false;
		boolean res = false;
		int xShifft = 0;
		int yShifft = 0;
		boolean sizeVerified = false;

		switch (randMoveChoise) {
		case 0:
			// tryUp
			xShifft = -1;
			yShifft = 0;
			sizeVerified = (currX - deck + 1 >= 0);
			break;
		case 1:
			// tryDown
			xShifft = 1;
			yShifft = 0;
			sizeVerified = (currX + deck - 1 < FIELD_SIZE);
			break;
		case 2:
			// tryLeft
			xShifft = 0;
			yShifft = -1;
			sizeVerified = (currY - deck + 1 >= 0);
		case 3:
			// tryRight
			xShifft = 0;
			yShifft = 1;
			sizeVerified = (currY + deck - 1 < FIELD_SIZE);
			break;
		}

		if (sizeVerified) {
			// at first we create 4-deck boat => it can be replaced anywhere,
			// so, its coordinates are correct (this boat doesn't touch another boat) and we
			// don't verify its cells' locations
			// but check that boats cells locations are correct for all not 4-deck boats
			if (deck < 4) {
				for (int l = 0; l < deck; l++) {
					correct = isCorrectLocation(currX + xShifft * l, currY + yShifft * l);
					if (!correct) {
						return false;
					}
				}
			}
			Boat boat = new Boat(deck);
			List<Coordinate> coordinates = new ArrayList<Coordinate> ();
			//add to boat coordinates of all its cells
			for (int i = 0; i < deck; i++) {
				fillCell(boat, currX, currY, color);
				coordinates.add(new Coordinate(currX, currY));
				currX += xShifft;
				currY += yShifft;
				res = true;
			}
			if (res) {
				boat.addCoordinates(coordinates);
				boats[boatsIndex++] = boat;
			}
		}
		return res;
	}

	protected boolean isCorrectLocation(int xx, int yy) {
		if (cells[xx][yy].hasBoat())
			return false;

		// check that left field, right field, above field, under field and
		// the nearest field by diagonals are not occupied by another boat
		int x;
		int y;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				x = xx + i;
				y = yy + j;
				if (x < 0 || x >= FIELD_SIZE || y >= FIELD_SIZE || y < 0)
					continue;

				if (cells[x][y].hasBoat()) {
					return false;
				}
			}
		}
		return true;
	}

	// common part for both person and computer players
	public boolean processShot(int x, int y) {

		cells[x][y].setShown(true);

		// not guessed at all
		if (!cells[x][y].hasBoat()) {
			cells[x][y].setState(State.PASSED);
			return false;
		}

		// there is boat in this cell (x,y)
		int guessed = 0;
		int deck = 0;
		List<Coordinate> coordinates = null;
		int i = 0;
		int j = 0;
		Boat boat = getBoat(x, y);

		if (boat != null) {
			boat.setInjured();
			guessed = boat.getGuessed();
			deck = boat.getDeck();

			// boat is killed
			// mark all its cells as KILLED, change their color and surround killed boat with points
			if (guessed == deck) {
				coordinates = boat.getCoordinates();
				for (int k = 0; k < coordinates.size(); k++) {
					i = coordinates.get(k).getX();
					j = coordinates.get(k).getY();
					cells[i][j].setColor(Color.RED);
					// System.out.println(" cell x: " + i + ", y: " + j + " KILLED !!!");
					cells[i][j].setState(State.KILLED);
					setPointsAround(i, j);
				}
				++killedBoats;
			} else {
				// just injured, not killed
				// mark all its cells as INJURED, change their color
				cells[x][y].setColor(new Color(240, 128, 128));
				cells[x][y].setState(State.INJURED);
				// System.out.println(" cell x: " + x + ", y: " + y + " INJURED !!!");
			}
		}
		return true;
	}

	// set points around killed boat
	protected void setPointsAround(int xx, int yy) {
		// fill out free cells located around killed cell (x,y) with points
		// verify left cell, right cell, above cell, under cell and
		// the nearest cells by diagonals
		int x = 0;
		int y = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				x = xx + i;
				y = yy + j;

				if (x < 0 || x >= FIELD_SIZE || y >= FIELD_SIZE || y < 0 || cells[x][y].hasBoat()
						|| cells[x][y].isInjured() || cells[x][y].isPassed() || cells[x][y].isKilled()) {
					continue;
				}
				cells[x][y].setState(State.PASSED);
				cells[x][y].setShown(true);
			}
		}
	}

	// find out what boat these coordinates are belong to
	protected Boat getBoat(int x, int y) {
		Boat boat;
		for (int i = 0; i < boats.length; i++) {
			boat = boats[i];
			List<Coordinate> coordinates = new ArrayList<Coordinate>();
			coordinates = boat.getCoordinates();

			for(Coordinate coord: coordinates)
				if (coord.getX() == x && coord.getY() == y) {
					return boat;
				}
			}
		return null;
	}

	public Color getColor(int i, int j) {
		return cells[i][j].getColor();
	}

}

package com.gmail.kramarenko104.seabattle.controller;

import java.util.ArrayList;
import java.util.List;
import com.gmail.kramarenko104.seabattle.model.Coordinate;

public class Person extends Player {

	private List<Coordinate> guessedXY;

	public Person(String name) {
		super(name);
		guessedXY = new ArrayList<Coordinate>();
	}

	@Override
	public boolean processShot(int x, int y) {
		boolean ret = super.processShot(x, y);
		// remember successful shot and start from the random cell again when boat is killed
		if (ret) {
			if (cells[x][y].isInjured()) {
				guessedXY.add(new Coordinate(x, y));
			} else if (cells[x][y].isKilled()) {
				guessedXY.clear();
			}
		}
		return ret;
	}

	// computer tries to guess where persons' boats are replaced
	public boolean processShot() {
		int x;
		int y;

		// exit 'while' loop when the suitable coordinate for the next shot is found
		while (true) {
			// !guessedXY.isEmpty() === it's not the first shot, there was already successful shot with injured enemy boat
			// continue to guess the rest of this injured boat around guessed coordinate
			if (!guessedXY.isEmpty()) {
				// randomly select what side (up, down, left, right) to move
				int randMoveChoise = (int) (Math.random() * 4);
				Coordinate nextTry = tryGuessNextMove(randMoveChoise);
				x = nextTry.getX();
				y = nextTry.getY();
				break;
			}
			else {  // create random coordinate for the new shot
				// start the new random point for search
				x = (int) (Math.random() * 10);
				y = (int) (Math.random() * 10);

				// don't process already processed cell, try the new one
				while (cells[x][y].isPassed() || cells[x][y].isInjured() || cells[x][y].isKilled()) {
					x = (int) (Math.random() * 10);
					y = (int) (Math.random() * 10);
				}
				break;
			}
		}
		return processShot(x, y);
	}

	private Coordinate tryGuessNextMove(int randMoveChoise) {
		// we are here because of we guessed already at least one cell, so, list 'guessedXY' is not empty
		boolean wrongPosition = true;
		int xFirstGuessed = guessedXY.get(0).getX();
		int yFirstGuessed = guessedXY.get(0).getY();
		int xLastGuessed = guessedXY.get(guessedXY.size() - 1).getX();
		int yLastGuessed = guessedXY.get(guessedXY.size() - 1).getY();
		int x = xLastGuessed;
		int y = yLastGuessed;
		int wrongTry = 0;

		// we have already minimum two near located guessed cells
		// so we already know correct direction: horizontally or vertically
		// continue to move in this direction
		if (guessedXY.size() > 1) {
			if (xFirstGuessed == xLastGuessed) {
				if (yFirstGuessed < yLastGuessed) {
					randMoveChoise = 3; // we continue to move right
				} else {
					randMoveChoise = 2; // we continue to move left
				}
			} else if (yFirstGuessed == yLastGuessed) {
				if (xFirstGuessed < xLastGuessed) {
					randMoveChoise = 1; // we continue to move down
				} else {
					randMoveChoise = 0; // we continue to move up
				}
			}
		}
		int xShifft = 0;
		int yShifft = 0;

		while (wrongPosition) {
//			System.out.println("--- enter with direction ... " + ((randMoveChoise == 0) ? "up"
//					: ((randMoveChoise == 1) ? "down" : ((randMoveChoise == 2) ? "left" : "right"))));
			switch (randMoveChoise) {
			case 0:
				// tryUp
				xShifft = -1;
				yShifft = 0;
				break;
			case 1:
				// tryDown
				xShifft = 1;
				yShifft = 0;
				break;
			case 2:
				// tryLeft
				xShifft = 0;
				yShifft = -1;
				break;
			case 3:
				// tryRight
				xShifft = 0;
				yShifft = 1;
				break;
			}
			x += xShifft;
			y += yShifft;
			// check if this new coordinate is correct
			wrongPosition = isWrogPosition(x, y);

			if (wrongPosition) {
				// We didn't found correct potential next shot. Try to start from the first guessed coordinate
				x = xFirstGuessed;
				y = yFirstGuessed;

				wrongTry++;
				// if we tried up-down or left-right, and both directions are wrong,
				// then try the new random direction to not get into endless loop
				if (wrongTry >= 2) {
					randMoveChoise = (int) (Math.random() * 4);
				} else {
					// continue to move correctly horizontally or vertically
					// but in the opposite direction
					randMoveChoise = ((randMoveChoise == 0) ? 1
							: (((randMoveChoise == 1) ? 0
									: ((randMoveChoise == 2) ? 3 : ((randMoveChoise == 3) ? 2 : 0)))));
				}
				// System.out.println("--- NEW direction is ... " + ((randMoveChoise == 0) ?
				// "up" : ((randMoveChoise == 1) ? "down" : ((randMoveChoise == 2) ? "left" : "right"))));
			}
		}
		// System.out.println("---We've found correct potential next shot: " + " (" + x + ", " + y + ")");
		return new Coordinate(x, y);

	}

	private boolean isWrogPosition(int x, int y) {
		return ((x < 0 || x >= FIELD_SIZE || y >= FIELD_SIZE || y < 0 || cells[x][y].isPassed()
				|| cells[x][y].isInjured() || cells[x][y].isKilled()));
	}
}

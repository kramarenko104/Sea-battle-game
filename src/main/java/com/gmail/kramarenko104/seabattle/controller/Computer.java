package com.gmail.kramarenko104.seabattle.controller;

public class Computer extends Player {
	
	public Computer(String name) {
		super(name);
	}

	@Override
	public boolean processShot(int x, int y) {

		if (cells[x][y].isInjured() || cells[x][y].isKilled() || cells[x][y].isPassed()) {
			return true;
		}
		return super.processShot(x, y);
	}
}

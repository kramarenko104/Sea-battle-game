package com.gmail.kramarenko104.seabattle.model;

public class Coordinate {
	int x;
	int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "Coordinate [x=" + x + ", y=" + y + "]";
	}

}
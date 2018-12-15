package com.gmail.kramarenko104.seabattle.model;

import java.awt.Color;
import com.gmail.kramarenko104.seabattle.model.Boat;

public class Cell {

	private boolean isShown;
	private boolean isBoat;
	private State state;
	private Color color;
	private Boat boat;

	public Cell(boolean isShown, boolean isBoat, Color color, State state) {
		this.isShown = isShown;
		this.isBoat = isBoat;
		this.color = color;
		this.state = state;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean hasBoat() {
		return isBoat;
	}

	public void setBoat(boolean isBoat) {
		this.isBoat = isBoat;
	}

	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}

	public Color getColor() {
		return color;
	}

	public boolean isKilled() {
		return (state.equals(State.KILLED) ? true : false);
	}

	public boolean isInjured() {
		return (state.equals(State.INJURED) ? true : false);
	}

	public boolean isPassed() {
		return (state.equals(State.PASSED) ? true : false);
	}

	public void setState(State state) {
		this.state = state;
	}

	public Boat getBoat() {
		return boat;
	}

}

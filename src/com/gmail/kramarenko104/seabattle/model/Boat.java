package com.gmail.kramarenko104.seabattle.model;

import java.util.ArrayList;
import java.util.List;

import com.gmail.kramarenko104.seabattle.model.Coordinate;

public class Boat {

	private List<Coordinate> coordinates;
	private int deck;
	private int guessed;

	public Boat(int deck) {
		coordinates = new ArrayList<Coordinate>();
		this.deck = deck;
		guessed = 0;
	}
	
	public int getGuessed() {
		return guessed;
	}
	
	public int getDeck() {
		return deck;
	}

	public int setInjured() {
		return (++guessed);
	}
	
	public void addCoordinates(List<Coordinate> coordinates) {
		this.coordinates = coordinates;	
	}
		
	public List<Coordinate> getCoordinates() {
			return coordinates;
	}

}

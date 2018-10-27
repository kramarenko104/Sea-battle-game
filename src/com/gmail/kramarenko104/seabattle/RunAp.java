package com.gmail.kramarenko104.seabattle;

import com.gmail.kramarenko104.seabattle.view.*;
import com.gmail.kramarenko104.seabattle.controller.*;

public class RunAp {

	public static void main(String[] args) {

		final int FIELD_SIZE = 10;

		Player.setArrSize(FIELD_SIZE);
		Person person = new Person(new DialogName().getName());
		Computer comp = new Computer("Computer");
		
		GameBoard board = new GameBoard(FIELD_SIZE);
		board.init(person, comp);
	}
}

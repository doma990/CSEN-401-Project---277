package game.engine.dataloader;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import game.engine.Role;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.monsters.*;

public class DataLoader {
	private static String CARDS_FILE_NAME = "cards.csv";
	private static String CELLS_FILE_NAME = "cells.csv";
	private static String MONSTERS_FILE_NAME = "monsters.csv";
		
	public static ArrayList<Card> readCards() throws IOException {
		ArrayList<Card> cards = new ArrayList<Card>();
		BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE_NAME));
		String line = br.readLine();
		while (line != null) {
			String[] temp = line.split(",");
			if (temp[0].equals("SWAPPER"))
				cards.add(new SwapperCard(temp[1], temp[2], Integer.parseInt(temp[3])));
			else if (temp[0].equals("SHIELD"))
				cards.add(new ShieldCard(temp[1], temp[2], Integer.parseInt(temp[3])));
			else if (temp[0].equals("ENERGYSTEAL"))
				cards.add(new EnergyStealCard(temp[1], temp[2], Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
			else if (temp[0].equals("STARTOVER"))
				cards.add(new StartOverCard(temp[1], temp[2], Integer.parseInt(temp[3]), Boolean.parseBoolean(temp[4])));
			else if (temp[0].equals("CONFUSION"))
				cards.add(new ConfusionCard(temp[1], temp[2], Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
			line = br.readLine();
		}
		br.close();
		return cards;
	}
	
	public static ArrayList<Cell> readCells() throws IOException {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		BufferedReader br = new BufferedReader(new FileReader(CELLS_FILE_NAME));
		String line = br.readLine();
		while (line != null) {
			String[] temp = line.split(",");
			if (temp.length == 3)
				cells.add(new DoorCell(temp[0], Role.valueOf(temp[1]), Integer.parseInt(temp[2])));
			else if (temp.length == 2)
				if (Integer.parseInt(temp[1]) > 0)
					cells.add(new ConveyorBelt(temp[0], Integer.parseInt(temp[1])));
				else
					cells.add(new ContaminationSock(temp[0], Integer.parseInt(temp[1])));					
			line = br.readLine();
		}
		br.close();
		return cells;
	}

	public static ArrayList<Monster> readMonsters() throws IOException {
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		BufferedReader br = new BufferedReader(new FileReader(MONSTERS_FILE_NAME));
		String line = br.readLine();
		while (line != null) {
			String[] temp = line.split(",");
			if (temp[0].equals("DYNAMO"))
				monsters.add(new Dynamo(temp[1], temp[2], Role.valueOf(temp[3]), Integer.parseInt(temp[4])));
			else if (temp[0].equals("DASHER"))
				monsters.add(new Dasher(temp[1], temp[2], Role.valueOf(temp[3]), Integer.parseInt(temp[4])));
			else if (temp[0].equals("SCHEMER"))
				monsters.add(new Schemer(temp[1], temp[2], Role.valueOf(temp[3]), Integer.parseInt(temp[4])));
			else if (temp[0].equals("MULTITASKER"))
				monsters.add(new MultiTasker(temp[1], temp[2], Role.valueOf(temp[3]), Integer.parseInt(temp[4])));
			line = br.readLine();
		}
		br.close();
		return monsters;
	}
}


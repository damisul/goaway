package ru.goproject.goaway.common;

import java.util.Vector;

public class Problem {
	private String title;
	private String difficulty;
	private String source;
	private String genre;
	private Vector nodes;
	private int size;

	public byte playerColor;
	public byte opponentColor;
	
	public Problem() {
		setPlayerColor(Stone.STONE_NONE);
		size = 19;
	}

	public void setPlayerColor(byte color) {
		playerColor = color;
		switch(color) {
		case Stone.STONE_BLACK: opponentColor = Stone.STONE_WHITE; break;
		case Stone.STONE_WHITE: opponentColor = Stone.STONE_BLACK; break;
		default: opponentColor = Stone.STONE_NONE;
		}		 
	}

	public void init(Vector problemNodes) {
		if (playerColor == Stone.STONE_NONE) {
			if (problemNodes.size() > 1) {
				MoveAction action = (MoveAction)((Node)problemNodes.elementAt(1)).getAction();
				setPlayerColor(action.getColor());
			}
		}
		this.nodes = problemNodes;
	}
	
	public Vector getNodeList() {
		return nodes;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

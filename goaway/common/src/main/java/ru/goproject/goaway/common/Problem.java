/**
 * Copyright (c) 2008-2013 Damir Sultanbekov All Rights Reserved. 
 * This file is part of GoAway project.
 * 
 * GoAway is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GoAway is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GoAway.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		setPlayerColor(Goban.STONE_NONE);
		size = 19;
	}

	public void setPlayerColor(byte color) {
		playerColor = color;
		switch(color) {
		case Goban.STONE_BLACK: opponentColor = Goban.STONE_WHITE; break;
		case Goban.STONE_WHITE: opponentColor = Goban.STONE_BLACK; break;
		default: opponentColor = Goban.STONE_NONE;
		}		 
	}

	public void init(Vector problemNodes) {
		if (playerColor == Goban.STONE_NONE) {
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
	
	public Rectangle getFullRectangle(int margin) {
		Rectangle problemRect = new Rectangle(
			Integer.MAX_VALUE,
			Integer.MAX_VALUE,
			Integer.MIN_VALUE,
			Integer.MIN_VALUE
		);
		for (int i = 0; i < nodes.size(); ++i) {
			Node n = (Node) nodes.elementAt(i);
			Vector labels = n.getLabels();
			if (labels != null) {
				for (int j = 0; j < labels.size(); ++j) {
					Label l = (Label) labels.elementAt(j);
					problemRect.resizeToIncludePoint(l.getPoint());
				}
			}
			if (n.getAction() instanceof EditorAction) {
				EditorAction a = (EditorAction) n.getAction();
				Vector stones = a.getAddedBlackStones();
				for (int j = 0; j < stones.size(); ++j) {
					problemRect.resizeToIncludePoint((Point) stones.elementAt(j));
				}
				stones = a.getAddedWhiteStones();
				for (int j = 0; j < stones.size(); ++j) {
					problemRect.resizeToIncludePoint((Point) stones.elementAt(j));
				}
			}
			if (n.getAction() instanceof MoveAction) {
				MoveAction a = (MoveAction) n.getAction();
				problemRect.resizeToIncludePoint(a.getPoint());
			}
		}

		if (problemRect.minX <= margin) {
			problemRect.minX = 0;
		} else {
			problemRect.minX -= margin;
		}

		if (problemRect.maxX > size - margin - 1) {
			problemRect.maxX = size - 1;
		} else {
			problemRect.maxX += margin;
		}

		if (problemRect.minY <= margin) {
			problemRect.minY = 0;
		} else {
			problemRect.minY -= margin;
		}

		if (problemRect.maxY > size - margin - 1) {
			problemRect.maxY = size - 1;
		} else {
			problemRect.maxY += margin;
		}
		return problemRect;
	}	
}

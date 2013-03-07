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

import ru.goproject.goaway.exception.GoAwayException;

public class Goban {
	private final int size;
	private final byte[][] board;
	Goban(int size) {
		this.size = size;
		board = new byte[size][size];
		for(byte i = 0; i < size; ++i) {
			for (byte j = 0; j < size; ++j) {
				board[i][j] = Stone.STONE_NONE;
			}
		}
	}

	public int getSize() {
		return size;
	}

	public byte getPointColor(int x, int y) {
		return board[x][y];
	}

	public byte getPointColor(Point pt) {
		return getPointColor(pt.getX(), pt.getY());
	}

	private void setPointColor(int x, int y, byte color) {
		board[x][y] = color;
	}

	private void setPointColor(Point pt, byte color) {
		setPointColor(pt.getX(), pt.getY(), color);
	}

	/**
	 * TODO: сделать приватным, после того, как избавлюсь от класса Group
	 */
	public Vector getNeighbours(Point pt, byte color) {
		Vector result = new Vector(4);
		int x = pt.getX(),
			y = pt.getY();
		if (x < size - 1) {
			if (getPointColor(x + 1, y) == color) {
				result.addElement(new Point(x + 1, y));
			}
		}
		if (x > 0) {
			if (getPointColor(x - 1, y) == color) {				
				result.addElement(new Point(x - 1, y));
			}
		}
		if (y > 0) {
			if (getPointColor(x, y - 1) == color) {
				result.addElement(new Point(x, y - 1));
			}
		}
		if (y < size - 1) {
			if (getPointColor(x, y + 1) == color) {
				result.addElement(new Point(x, y + 1));
			}
		}
		return result;
	}
	
	public boolean isMoveAllowed(Point point, byte color) {
		// если на этом месте уже есть камень, то ход запрещен
		if (getPointColor(point) != Stone.STONE_NONE) {
			return false;
		}
		
		// если к выбранному пункту прилегает хотя бы один свободный пункт, то ход разрешен 
		// так как он не может быть самоубийственным
		Vector points = getNeighbours(point, Stone.STONE_NONE);
		if (!points.isEmpty()) {
			return true;
		}
				
		byte opponentColor = color == Stone.STONE_BLACK ? Stone.STONE_WHITE : Stone.STONE_BLACK;		
		points = getNeighbours(point, opponentColor);
		for (int i = 0; i < points.size(); ++i) {
			Point pt = (Point)points.elementAt(i);
			Group g = new Group(this, pt.getX(), pt.getY());
			// если у одной из групп противника, прилегающих к выбранному пункту доски
			// осталось только одно даме, то ход разрешен, так как он приведет к снятию этой
			// группы противника
			// TODO: проверка на ко
			if (g.getGroupDame(this) == 1) {
				return true;
			}
		}
		
		points = getNeighbours(point, color);
		for (int i = 0; i < points.size(); ++i) {
			Point pt = (Point)points.elementAt(i);
			Group g = new Group(this, pt.getX(), pt.getY());
			if (g.getGroupDame(this) > 1) {
				return true;
			}
		}
		// если дошли сюда, то ход является самоубийственным
		return false;
	}

	/**
	 * Сделать ход
	 * @param stone параметры хода
	 * @return массив точек с координатами съеденных камней противника или null, если съеденных камней не было
	 */
	public Vector doMove(Point stone, byte color) throws GoAwayException {
		Vector eatenStones = new Vector();
		byte opponentColor = color == Stone.STONE_BLACK ? Stone.STONE_WHITE : Stone.STONE_BLACK;

		int x = stone.getX(),
			y = stone.getY();
		
		if (getPointColor(stone) != Stone.STONE_NONE) {
			throw new GoAwayException("Couldn't play at the existing stone");
		} else {
			setPointColor(stone, color);
		}
		eatIfPossible(x - 1, y, opponentColor, eatenStones);
		eatIfPossible(x + 1, y, opponentColor, eatenStones);
		eatIfPossible(x, y + 1, opponentColor, eatenStones);
		eatIfPossible(x, y - 1, opponentColor, eatenStones);
		Group g = new Group(this, x, y);
		if (!g.isAlive(this)) {
			setPointColor(stone, Stone.STONE_NONE);
			throw new GoAwayException("Forbidden move");
		}
		return eatenStones.size() > 0? eatenStones : null;
	}

	public void addStones(Vector points, byte color) throws GoAwayException {
		for (int i = 0; i < points.size(); ++i) {
			Point pt = (Point)points.elementAt(i);
			addStone(pt, color);
		}
	}
	
	public void addStone(Point pt, byte color) throws GoAwayException {
		if (getPointColor(pt) != Stone.STONE_NONE) {
			throw new GoAwayException("No place to add stone");
		}
		setPointColor(pt, color);
	}

	public void removeStone(Point pt, byte color) throws GoAwayException {
		if (getPointColor(pt) != color) {
			throw new GoAwayException("Stone not found");
		} else {
			setPointColor(pt, Stone.STONE_NONE);	
		}
	}

	public void removeStones(Vector points, byte color) {

	}

	private void eatIfPossible(int x, int y, byte opponentColor, Vector eatenStones) throws GoAwayException {
		if (x < 0 || x >= size || y < 0 || y >= size) {
			return;
		}
		if (getPointColor(x, y) != opponentColor) {
			return;
		}
		Group g = new Group(this, x, y);
		if (!g.isAlive(this)) {
			Vector points = g.getPoints();
			for(int i = 0; i < points.size(); ++i) {
				Point p = (Point)points.elementAt(i);
				removeStone(p, opponentColor);
				setPointColor(p, Stone.STONE_NONE);
				eatenStones.addElement(p);
			}
		}
	}

}

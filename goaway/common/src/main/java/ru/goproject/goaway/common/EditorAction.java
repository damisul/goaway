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

public class EditorAction implements NodeAction {
	private Vector addedBlackStones;
	private Vector addedWhiteStones;
	
	public Vector getAddedBlackStones() {
		return addedBlackStones;
	}
	
	public Vector getAddedWhiteStones() {
		return addedWhiteStones;
	}
	
	public EditorAction(Vector abs, Vector aws) {
		addedBlackStones = abs != null ? abs : new Vector();
		addedWhiteStones = aws != null ? aws : new Vector();
	}
	
	public void addWhiteStones(Vector points) {
		int sz = points.size();
		for (int i = 0; i < sz; ++ i) {
			addedWhiteStones.addElement(points.elementAt(i));
		}
	}
	
	public void addBlackStones(Vector points) {
		int sz = points.size();
		for (int i = 0; i < sz; ++ i) {
			addedBlackStones.addElement(points.elementAt(i));
		}
	}

	public void apply(Goban goban)  throws GoAwayException {
		int sz = addedBlackStones.size();
		for (int i = 0; i < sz; ++i) {
			Point stone = (Point)addedBlackStones.elementAt(i);
			goban.addStone(stone, Goban.STONE_BLACK);
		}
		
		sz = addedWhiteStones.size();
		for (int i = 0; i < sz; ++i) {
			Point stone = (Point)addedWhiteStones.elementAt(i);
			goban.addStone(stone, Goban.STONE_WHITE);
		}
		
	}

	public void undo(Goban goban) {
		// TODO: писать реализацию undo
		// пока не нужно, так как в задачах
		// редактирование возможно только в первом узле - при расстановке начально позиции
	}
}

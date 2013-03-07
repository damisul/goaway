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

public class MoveAction implements NodeAction {
	private Point point;
	private byte color;
	private byte opponentColor;
	private Vector eatenStones;
	
	public MoveAction(Point point, byte color) {
		this.point = point;
		this.color = color;
		opponentColor = color == Goban.STONE_BLACK ? Goban.STONE_WHITE : Goban.STONE_BLACK;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public boolean isPass() {
		return point == null;
	}
	
	public void apply(Goban goban) throws GoAwayException {
		if (isPass()) {
			return;
		}
		eatenStones = goban.doMove(point, color);
	}
		
	public void undo(Goban goban) throws GoAwayException {
		if (isPass()) {
			return;
		}
		goban.removeStone(point, color);
		if (eatenStones != null) {
			goban.addStones(eatenStones, opponentColor);
			eatenStones = null;
		}
	}
	
	public byte getColor() {
		return color;
	}
}

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

public class Group {
	private byte color;
	private Vector points;
	public Group(Goban goban, int x, int y) {
		color = goban.getPointColor(x, y);
		points = new Vector();
		points.addElement(new Point(x, y));
		for(int i = 0; i < points.size(); ++i) {
			Point pt = (Point)points.elementAt(i);
			Vector neighbours = goban.getNeighbours(pt, color);
			for (int j = 0; j < neighbours.size(); ++j) {
				Point neighbour = (Point)neighbours.elementAt(j);
				if (!points.contains(neighbour)) {
					points.addElement(neighbour);
				}
			}
		}
	}
	
	public int getGroupDame(Goban goban) {
		Vector dame = new Vector();		
		for(int i = 0; i < points.size(); ++i) {
			Point pt = (Point)points.elementAt(i);
			Vector neighbours = goban.getNeighbours(pt, Stone.STONE_NONE);
			for (int j = 0; j < neighbours.size(); ++j) {
				Point neighbour = (Point)neighbours.elementAt(j);
				if (!dame.contains(neighbour)) {
					dame.addElement(neighbour);
				}
			}
		}
		return dame.size();
	}
	
	public boolean isAlive(Goban goban) {
		if (color == Stone.STONE_NONE) {
			return true;
		}
		for (int i = 0; i < points.size(); ++i) {
			Point pt = (Point) points.elementAt(i);
			if (goban.getNeighbours(pt, Stone.STONE_NONE).size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public Vector getPoints() {
		return points;
	}
}

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

public class Point {
	public int x;
	public int y;
	
	/**
	 * @deprecated
	 */
	public final int getX() {
		return x;
	}

	/**
	 * @deprecated
	 */	
	public final void setX(int x) {
		this.x = x;
	}
	
	/**
	 * @deprecated
	 */
	public final int getY() {
		return y;
	}
	
	/**
	 * @deprecated
	 */	
	public final void setY(int y) {
		this.y = y;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point pt) {
		this.x = pt.x;
		this.y = pt.y;
	}
	
	public boolean equals(Object o) {
		Point pt = (Point)o;
		return pt != null && x == pt.x && y == pt.y;
	}
	
	public int hashCode() {
		return x + y*100;
	}
}

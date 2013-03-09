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

public class Rectangle {
	public int minX;
	public int maxX;
	public int minY;
	public int maxY;
	
	public Rectangle() {
	}
	
	public Rectangle(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public Rectangle(Rectangle rect) {
		this(rect.minX, rect.minY, rect.maxX, rect.maxY);
	}
	
	public Point getCenter() {
		return new Point((maxX + minX) / 2, (maxY + minY) / 2);
	}
	
	public void resizeToIncludePoint(Point pt) {
		if (pt.x < minX) {
			minX = pt.x;
		}
		if (pt.x > maxX) {
			maxX = pt.x;
		}
		if (pt.y < minY) {
			minY = pt.y;
		}
		if (pt.y > maxY) {
			maxY = pt.y;
		}
	}

	public void moveToIncludePoint(Point pt) {
		int dx = 0,
			dy = 0;
		if (pt.x < minX) {				
			dx = pt.x - minX;
		} else if (pt.x > maxX) {
			dx = pt.x - maxX;
		}
		if (pt.y < minY) {				
			dy = pt.y - minY;
		} else if (pt.y > maxY) {
			dy = pt.y - maxY;
		}
		move(dx, dy);
	}
	
	public int getWidth() {
		return maxX - minX + 1;
	}
	
	public int getHeight() {
		return maxY - minY + 1;
	}
	
	public boolean contains(Point pt) {
		return contains(pt.x, pt.y);
	}
	
	public boolean contains(int x, int y) {
		return x >= minX && y >= minY && x <= maxX && y <= maxY;
	}
	
	public void move(int dx, int dy) {
		minX += dx;
		maxX += dx;
		minY += dy;
		maxY += dy;
	}
}

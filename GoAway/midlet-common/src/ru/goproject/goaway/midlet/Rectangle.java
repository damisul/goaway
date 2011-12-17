package ru.goproject.goaway.midlet;

import ru.goproject.goaway.common.Point;

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
	
	public boolean contain(Point pt) {
		return pt.x >= minX && pt.y >= minY && pt.x <= maxX && pt.y <= maxY;
	}
	
	public void move(int dx, int dy) {
		minX += dx;
		maxX += dx;
		minY += dy;
		maxY += dy;
	}
}

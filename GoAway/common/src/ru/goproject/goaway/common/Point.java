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

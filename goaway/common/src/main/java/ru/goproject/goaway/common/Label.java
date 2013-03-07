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

public class Label {
	public final static byte LABEL_TEXT = 0;
	public final static byte LABEL_CIRCLE = 1;
	public final static byte LABEL_TRIANGLE = 2;
	public final static byte LABEL_SQUARE = 3;
	public final static byte LABEL_MARK = 4;
	Point point;
	String label;
	byte type;
	
	public Label(Point pt, String lbl) {
		point = pt;
		type = LABEL_TEXT;
		label = lbl;
	}
	
	public Label(Point pt, byte t) {
		point = pt;
		type = t;
	}
	
	public String getLabel() {
		return label;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public byte getType() {
		return type;
	}
}

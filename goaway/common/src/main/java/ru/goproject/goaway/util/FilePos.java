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
package ru.goproject.goaway.util;

public class FilePos {
	private int position;
	private int line;
	private int column;	
	
	public FilePos(int position, int line, int column) {
		this.position = position;
		this.column = column;
		this.line = line;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getLine() {
		return line;
	}	

	public int getColumn() {
		return column;
	}
}

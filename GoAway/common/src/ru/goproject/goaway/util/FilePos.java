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

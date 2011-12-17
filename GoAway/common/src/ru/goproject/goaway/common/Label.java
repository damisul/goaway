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

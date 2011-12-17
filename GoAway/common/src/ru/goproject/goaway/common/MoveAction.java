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
		opponentColor = color == Stone.STONE_BLACK ? Stone.STONE_WHITE : Stone.STONE_BLACK;
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

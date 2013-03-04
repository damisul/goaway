package ru.goproject.goaway.common;

import java.util.Vector;

import ru.goproject.goaway.exception.GoAwayException;

public class EditorAction implements NodeAction {
	private Vector addedBlackStones;
	private Vector addedWhiteStones;
	
	public Vector getAddedBlackStones() {
		return addedBlackStones;
	}
	
	public Vector getAddedWhiteStones() {
		return addedWhiteStones;
	}
	
	public EditorAction(Vector abs, Vector aws) {
		addedBlackStones = abs != null ? abs : new Vector();
		addedWhiteStones = aws != null ? aws : new Vector();
	}
	
	public void addWhiteStones(Vector points) {
		int sz = points.size();
		for (int i = 0; i < sz; ++ i) {
			addedWhiteStones.addElement(points.elementAt(i));
		}
	}
	
	public void addBlackStones(Vector points) {
		int sz = points.size();
		for (int i = 0; i < sz; ++ i) {
			addedBlackStones.addElement(points.elementAt(i));
		}
	}

	public void apply(Goban goban)  throws GoAwayException {
		int sz = addedBlackStones.size();
		for (int i = 0; i < sz; ++i) {
			Point stone = (Point)addedBlackStones.elementAt(i);
			goban.addStone(stone, Stone.STONE_BLACK);
		}
		
		sz = addedWhiteStones.size();
		for (int i = 0; i < sz; ++i) {
			Point stone = (Point)addedWhiteStones.elementAt(i);
			goban.addStone(stone, Stone.STONE_WHITE);
		}
		
	}

	public void undo(Goban goban) {
		// TODO: писать реализацию undo
		// пока не нужно, так как в задачах
		// редактирование возможно только в первом узле - при расстановке начально позиции
	}
}

package ru.goproject.goaway.common;

import ru.goproject.goaway.exception.GoAwayException;

public interface NodeAction {
	void apply(Goban goban) throws GoAwayException;
	void undo(Goban goban) throws GoAwayException;
}
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
package ru.goproject.goaway.sgf;

import java.util.Hashtable;

public class SgfProperty {
	public static final byte PROPERTY_TYPE_SIMPLE = 1;
	public static final byte PROPERTY_TYPE_LIST = 2;
	public static final byte PROPERTY_TYPE_ELIST = 3;

	public static final byte VALUE_TYPE_TEXT = 1;
	public static final byte VALUE_TYPE_INT = 2;
	public static final byte VALUE_TYPE_POINT = 3;
	public static final byte VALUE_TYPE_COLOR = 4;
	public static final byte VALUE_TYPE_POINT_AND_TEXT = 5;
	public static final byte VALUE_TYPE_POINT_OR_PASS = 6;

	public static final String SIZE = "SZ";
	public static final String ADD_WHITE_STONES = "AW";
	public static final String ADD_BLACK_STONES = "AB";
	public static final String COMMENT = "C";
	public static final String BLACK_MOVE = "B";
	public static final String WHITE_MOVE = "W";
	public static final String PLAYER_COLOR = "PL";
	public static final String LABEL = "LB";
	public static final String CIRCLE = "CR";
	public static final String TRIANGLE = "TR";
	public static final String SQUARE = "SQ";
	public static final String MARK = "MA";
	public static final String CHARACTER_ENCODING = "CA";
	public static final String GENRE = "GE";
	public static final String SOURCE = "SO";
	public static final String DIFFICULTY = "DI";
	public static final String WRONG_VARIATION = "WV";
	private static final Hashtable properties;

	public final byte type;
	public final byte valueType;
	private SgfProperty(byte type, byte valueType) {
		this.type = type;
		this.valueType = valueType;
	}
	
	static {
		properties = new Hashtable();
		properties.put(WHITE_MOVE, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_POINT_OR_PASS));
		properties.put(BLACK_MOVE, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_POINT_OR_PASS));
		properties.put(COMMENT, new SgfProperty(PROPERTY_TYPE_ELIST, VALUE_TYPE_TEXT));
		properties.put(LABEL, new SgfProperty(PROPERTY_TYPE_LIST, VALUE_TYPE_POINT_AND_TEXT));
		properties.put(CIRCLE, new SgfProperty(PROPERTY_TYPE_LIST, VALUE_TYPE_POINT));
		properties.put(TRIANGLE, new SgfProperty(PROPERTY_TYPE_LIST, VALUE_TYPE_POINT));
		properties.put(SQUARE, new SgfProperty(PROPERTY_TYPE_LIST, VALUE_TYPE_POINT));
		properties.put(MARK, new SgfProperty(PROPERTY_TYPE_LIST, VALUE_TYPE_POINT));
		properties.put(SIZE, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_INT));
		properties.put(ADD_WHITE_STONES, new SgfProperty(PROPERTY_TYPE_LIST, VALUE_TYPE_POINT));
		properties.put(ADD_BLACK_STONES, new SgfProperty(PROPERTY_TYPE_LIST, VALUE_TYPE_POINT));
		properties.put(PLAYER_COLOR, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_COLOR));
		properties.put(CHARACTER_ENCODING, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_TEXT));
		properties.put(GENRE, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_TEXT));
		properties.put(SOURCE, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_TEXT));
		properties.put(DIFFICULTY, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_TEXT));
		properties.put(WRONG_VARIATION, new SgfProperty(PROPERTY_TYPE_SIMPLE, VALUE_TYPE_TEXT));
	}

	public static SgfProperty getPropertyParams(String propName) {
		return (SgfProperty)properties.get(propName);
	}
}
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

import ru.goproject.goaway.common.EditorAction;
import ru.goproject.goaway.common.Goban;
import ru.goproject.goaway.common.Label;
import ru.goproject.goaway.common.MoveAction;
import ru.goproject.goaway.common.Node;
import ru.goproject.goaway.common.NodeAction;
import ru.goproject.goaway.common.Point;
import ru.goproject.goaway.common.Problem;
import ru.goproject.goaway.util.SmartReader;

public class ProblemReader {
	public static final String RAW_ENCODING = "ISO8859_1";
	private static final Point PASS_MOVE = new Point(0, 0);	
	private SmartReader reader;
	private Problem problem;
	private String problemEncoding;
	private boolean initCompleted;
	private Vector nodes;
	private Vector wrongNodes;
	private Node previousNode;
	private Node root;

	public static Problem readProblem(InputStream stream) throws IOException {
		SmartReader reader = new SmartReader(stream, RAW_ENCODING);
		ProblemReader r = new ProblemReader();
		Problem p = r.read(reader);
		reader.skipBlanks();
		boolean isEof = reader.isEof(); 
		reader.close();
		if (!isEof) {
			throw new ParseError("Data after end of problem");
		}		
		return p;
	}
	
	public synchronized Problem read(SmartReader reader) throws IOException {
		this.reader = reader;
		initCompleted = false;
		root = null;
		previousNode = null;
		problemEncoding = null;

		problem = new Problem();
		nodes = new Vector();
		wrongNodes = new Vector();		
		
		reader.skipBlanks();
		readTree(true);
		problem.init(nodes);
		
		if (!wrongNodes.isEmpty()) {
			processUliGoFormat();
		} else {
			boolean flag = processGoProblemsFormat();
			if (!flag) {
				processUliGoFormat();
			}
		}
		return problem;	
	}
	
	private void readTree(boolean root) throws IOException {
		reader.read('(');
		readSequence();
		
		if (root && !initCompleted) {
			if (this.root == null) {
				throw new ParseError("Couldn't find root node");
			} else {
				initCompleted = true;
			}
		}
		
		Node parent = (Node)nodes.lastElement();
		while (reader.getCurrentChar() != ')') {
			previousNode = parent;
			readTree(false);
		}
		reader.read(')');
		previousNode = parent;
		reader.skipBlanks();		 
	}	
	
	private void readSequence() throws IOException {
		reader.skipBlanks();
		boolean first = true;
		// COMPATIBILITY
		// Некоторые редакторы используют ';' не для обозначения начала узла,
		// а для разделения узлов, поэтому первый узел может начинаться сразу 
		// с идентификатора свойства
		while (first || reader.getCurrentChar() == ';') {
			first = false;
			readNode();
		}
		reader.skipBlanks();
	}
	
	public void readNode() throws IOException {
		// COMPATIBILITY
		// Во многих файлах задач встречаются пустые узлы. Их просто пропускаем.
		while (reader.getCurrentChar() == ';') {			
			reader.next();
			reader.skipBlanks();
		}
		Hashtable props = new Hashtable();
		while (Character.isUpperCase(reader.getCurrentChar())) {
			readProperty(props);
		}
		
		if (props.isEmpty()) {
			// COMPATIBILITY
			// Бывают не только пустые узлы, но и пустые последовательности, например (;)
			// В этом случае попадем сюда
			return;
		}		
		
		if (!initCompleted && (props.containsKey(SgfProperty.BLACK_MOVE) || props.containsKey(SgfProperty.WHITE_MOVE))) {
			initCompleted = true;
		}
		
		Node node = null;
		
		if (!initCompleted) {
			Integer size = (Integer)props.get(SgfProperty.SIZE);
			if (size != null) {
				problem.setSize(size.intValue());
			}
			String st = (String)props.get(SgfProperty.CHARACTER_ENCODING);
			if (st != null) {
				problemEncoding = st;
			}
			Byte playerColor = (Byte)props.get(SgfProperty.PLAYER_COLOR);
			if (playerColor != null) {
				problem.setPlayerColor(playerColor.byteValue());
			}
			
			st = (String)props.get(SgfProperty.GENRE);
			if (st != null) {
				problem.setGenre(st);
			}
			st = (String)props.get(SgfProperty.SOURCE);
			if (st != null) {
				problem.setSource(st);
			}
			st = (String)props.get(SgfProperty.DIFFICULTY);
			if (st != null) {
				problem.setDifficulty(st);
			}
			
			Vector abs = (Vector)props.get(SgfProperty.ADD_BLACK_STONES),
				aws = (Vector)props.get(SgfProperty.ADD_WHITE_STONES);
			
			if (root == null) {
				root = new Node(null, false);
				EditorAction action = new EditorAction(abs, aws);
				root.setAction(action);
				nodes.addElement(root);				
			} else {
				EditorAction action = (EditorAction)root.getAction();
				if (abs != null) {
					action.addBlackStones(abs);
				}
				if (aws != null) {
					action.addWhiteStones(aws);
				}
			}
			node = root;			
		} else {
			Point pt = null;
			byte color = Goban.STONE_NONE;			
			if (props.containsKey(SgfProperty.BLACK_MOVE)) {
				color = Goban.STONE_BLACK;
				pt = (Point)props.get(SgfProperty.BLACK_MOVE);
				if (props.containsKey(SgfProperty.WHITE_MOVE)) {
					throw new ParseError("Two moves in one node");					
				}
			} else if (props.containsKey(SgfProperty.WHITE_MOVE)) {
				color = Goban.STONE_WHITE;
				pt = (Point)props.get(SgfProperty.WHITE_MOVE);
			}
			if (pt == PASS_MOVE) {
				pt = null;
			}
			
			boolean bad = color == Goban.STONE_NONE || (props.containsKey(SgfProperty.ADD_BLACK_STONES) 
				|| props.containsKey(SgfProperty.ADD_WHITE_STONES)
			);
			
			if (bad) {
				throw new ParseError("Illegal node type");
			}

			node = new Node(previousNode, true);
			
			if (props.get(SgfProperty.WRONG_VARIATION) != null) {
				wrongNodes.addElement(node);
			}
			
			MoveAction action = new MoveAction(pt, color);
			node.setAction(action);
			nodes.addElement(node);			
		}
		readCommonProperties(node, props);		
		previousNode = node;
		reader.skipBlanks();
	}
	
	public void readProperty(Hashtable props) throws IOException {
		String propIdent = reader.readTill('[');
		if ("".equals(propIdent)) {
			throw new ParseError("Couldn't find property identifier");
		}
		propIdent = propIdent.trim();
		for (int i = 0; i < propIdent.length(); ++i) {
			if (!Character.isUpperCase(propIdent.charAt(i))) {
				throw new ParseError("Invalid property identifier: " + propIdent);
			}
		}
		
		SgfProperty prop = SgfProperty.getPropertyParams(propIdent);

		if (prop == null) {
			// Если неизвестное свойство, то просто пропускаем его
			readListPropVal(true, SgfProperty.VALUE_TYPE_TEXT, null);
		} else if (prop.type == SgfProperty.PROPERTY_TYPE_SIMPLE) {
			if (props.containsKey(propIdent)) {
				throw new ParseError("Duplicate property: " + propIdent);
			}
			props.put(propIdent, readSimplePropVal(false, prop.valueType));
		} else {			
			boolean allowEmptyResult = prop.type == SgfProperty.PROPERTY_TYPE_ELIST;
			Vector values = (Vector)props.get(propIdent);
			if (values == null) {
				values = new Vector();
				props.put(propIdent, values);
			}
			readListPropVal(allowEmptyResult, prop.valueType, values);
		}
	}
	
	private void readListPropVal(boolean allowEmptyResult, byte valueType, Vector values) throws IOException {
		Object val = readSimplePropVal(allowEmptyResult, valueType);
		if (val == null) {
			return;
		}
		if (values != null) {
			values.addElement(val);
		}
		while (reader.getCurrentChar() == '[') {
			val = readSimplePropVal(false, valueType);
			if (values != null) {
				values.addElement(val);
			}
		}
	}
	
	/**
	 * @param allowNull
	 * @param valueType
	 * @return
	 * @throws IOException
	 */
	private Object readSimplePropVal(boolean allowNull, byte valueType) throws IOException {
		reader.read('[');
		String val = reader.readTill(']', '\\');
		reader.next();
		reader.skipBlanks();
		if ("".equals(val) && allowNull) {
			return null;
		}		
		return getCastedPropVal(val, valueType);
	}
	
	private Object getCastedPropVal(String propValue, byte valueType) {
		switch (valueType) {		
		case SgfProperty.VALUE_TYPE_TEXT:
			// TODO:
			// по спецификации sgf, здесь нужно добавить преобразование пробелов
			// и переносов строк
			return propValue.trim();
		case SgfProperty.VALUE_TYPE_INT:
			return Integer.valueOf(propValue);
		case SgfProperty.VALUE_TYPE_COLOR:
			// COMPATIBILITY
			// в некоторых файлах цвет обозначается не буквой, а цифрой (используется тип SGF Double вместо Color)
			if ("B".equals(propValue) || "1".equals(propValue)) {
				return new Byte(Goban.STONE_BLACK);
			} else if ("W".equals(propValue) || "2".equals(propValue)) {
				return new Byte(Goban.STONE_WHITE);
			} else {
				throw new ParseError("Illegal color: " + propValue);				
			}
		case SgfProperty.VALUE_TYPE_POINT:
			return getPoint(propValue);
		case SgfProperty.VALUE_TYPE_POINT_AND_TEXT:
			if (propValue.length() < 4 || propValue.charAt(2) != ':') {
				throw new ParseError("illegal property value: " + propValue);
			}			
			Point pt = getPoint(propValue.substring(0, 2));
			String st = propValue.substring(3).trim();
			return new Label(pt, st);
		case SgfProperty.VALUE_TYPE_POINT_OR_PASS:
			if ("".equals(propValue) || (problem.getSize() <= ('t' - 'a' + 1) && "tt".equals(propValue))) {
				return PASS_MOVE;
			} else {
				return getPoint(propValue);
			}
		default:
			// Попасть сюда можем только в случае ошибки в программе
			throw new ParseError("Invalid value type: " + valueType);
		}
	}
	
	private Point getPoint(String st) {
		// TODO: в FF[4] для обозначения координат можно использовать и буквы в верхнем регистре,
		// таким образом максимальный размер доски может достигать 52x52
		if (st.length() != 2 || !Character.isLowerCase(st.charAt(0)) || !Character.isLowerCase(st.charAt(1))) {
			throw new ParseError("Illegal point definition: " + st);
		}
		
		int x = st.charAt(0) - 'a',
			y = st.charAt(1) - 'a'; 
		
		if (x > problem.getSize() || x < 0 || y > problem.getSize() || y < 0) {
			throw new ParseError("Point is outside of the board: " + st);
		}
		return new Point(x, y);
	}
	
	private void readCommonProperties(Node node, Hashtable props) {
		Vector comments = (Vector)props.get(SgfProperty.COMMENT);
		if (comments != null) {
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i<comments.size(); ++i) {
				String comment = (String)comments.elementAt(i);
				if (problemEncoding != null) {
					try {
						comment = new String(comment.getBytes(RAW_ENCODING), problemEncoding);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						problemEncoding = null;
					}
				}
				if (comment != null) {
					sb.append(comment);
				}
			}
			if (sb.length() > 0) {
				node.setComment(sb.toString());
			}
		}
 
		Vector labels = (Vector)props.get(SgfProperty.LABEL);
		if (labels == null) {
			labels = new Vector();
		}
		addLabels(labels, (Vector)props.get(SgfProperty.CIRCLE), Label.LABEL_CIRCLE);
		addLabels(labels, (Vector)props.get(SgfProperty.TRIANGLE), Label.LABEL_TRIANGLE);
		addLabels(labels, (Vector)props.get(SgfProperty.SQUARE), Label.LABEL_SQUARE);
		addLabels(labels, (Vector)props.get(SgfProperty.MARK), Label.LABEL_MARK);
		if (!labels.isEmpty()) {
			node.setLabels(labels);
		}
	}

	private void addLabels(Vector labels, Vector points, byte type) {		
		if (points != null) {
			int sz = points.size();
			for (int i = 0; i < sz; ++i) {
				Point pt = (Point) points.elementAt(i);
				labels.addElement(new Label(pt, type));
			}
		}		
	}
	
	public boolean processGoProblemsFormat() {
		Vector nodes = problem.getNodeList();
		int sz = nodes.size();
		boolean found = false;
		for (int i = 0; i < sz; ++i) {
			Node n = (Node)nodes.elementAt(i);
			n.markWrong();
		}
		
		for (int i = 0; i < sz; ++i) {
			Node n = (Node)nodes.elementAt(i);
			String comment = n.getComment();
			if (comment == null) {
				continue;
			}
			int idx = comment.indexOf("RIGHT"); 
			if (idx != -1) {
				found = true;
				n.markRight();
				comment = (comment.substring(0, idx) + comment.substring(idx + 5)).trim();
				n.setComment(comment);
				Node p = n.getParent();
				while (p != null && !p.isRight()) {
					p.markRight();
					p = p.getParent();
				}				
			}
		}
		
		if (!found) {
			return false;
		}
		
		found = false;
		for (int i = 0; i < sz; ++i) {
			Node n = (Node)nodes.elementAt(i);
			String comment = n.getComment();
			if (comment == null) {
				continue;
			}
			int idx = comment.indexOf("FORCE");
			if (idx != -1) {
				comment = (comment.substring(0, idx) + comment.substring(idx + 5)).trim();
				n.markForce();
			}
			idx = comment.indexOf("CHOICE");
			if (idx != -1) {
				comment = (comment.substring(0, idx) + comment.substring(idx + 6)).trim();
				n.markChoice();
				found = true;
			}
			idx = comment.indexOf("NOTTHIS");
			if (idx != -1) {
				comment = (comment.substring(0, idx) + comment.substring(idx + 7)).trim();
				n.markNotThis();
			}
			comment = comment.trim();
			if (comment.length() > 0) {
				n.setComment(comment);
			} else {
				n.setComment(null);
			}
		}
		return true;
	}
	
	private void markWrong(Node node) {
		if (!node.isRight()) {
			return;
		}
		node.markWrong();
		Vector children = node.getChildren();
		int sz = children.size();
		for (int i = 0; i < sz; ++i) {
			Node child = (Node)children.elementAt(i);
			markWrong(child);
		}
	}
	
	private void processUliGoFormat() {
		Vector nodes = problem.getNodeList();
		int sz = nodes.size();
		for (int i = 0; i < sz; ++i) {
			Node node = (Node)nodes.elementAt(i);
			node.markRight();
		}
		
		if (wrongNodes.size() > 0) {
			int wrongSz = wrongNodes.size();
			for (int i = 0; i < wrongSz; ++i) {
				Node wrongNode = (Node)wrongNodes.elementAt(i);
				markWrong(wrongNode);
			}
		} else {
			for (int i = 0; i < sz; ++i) {
				Node node = (Node)nodes.elementAt(i);
				NodeAction action = node.getAction();
				if (action instanceof MoveAction) {					
					Point pt = ((MoveAction)action).getPoint();
					if (pt == null) {
						continue;
					}
					Vector labels = node.getLabels();
					if (labels == null) {
						continue;
					}
					int labelSz = node.getLabels().size();
					for (int j = 0; j < labelSz; ++j) {
						Label label = (Label)labels.elementAt(j);
						if (label.getType() == Label.LABEL_TRIANGLE && label.getPoint().equals(pt)) {
							labels.removeElementAt(j);
							markWrong(node);
							break;
						}
					}
				}
			}
		}
	}	
}
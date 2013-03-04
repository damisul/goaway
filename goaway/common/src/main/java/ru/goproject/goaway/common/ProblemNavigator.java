package ru.goproject.goaway.common;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

import ru.goproject.goaway.exception.GoAwayException;

public class ProblemNavigator {
	public static byte STATE_SOLVING = 0;
	public static byte STATE_SOLVED = 1;
	public static byte STATE_WRONG = 2;

	private byte state;
	private Node root;
	private Goban goban;
	private Node current;	
		
	
	private static Random rand;
	static {
		rand = new Random();
		rand.setSeed((new Date()).getTime());
	}
	
	Problem problem;	
	public void setProblem(Problem problem) throws GoAwayException {
		state = STATE_SOLVING;
		this.problem = problem;
		if (problem != null) {
			goban = new Goban(problem.getSize());
			root = (Node)problem.getNodeList().elementAt(0);
			root.getAction().apply(goban);
		} else {
			goban = new Goban(9);
			root = null;
		}
		current = root;
	}
	
	public void move(Point pt) throws GoAwayException {
		if (state != STATE_SOLVING) {
			return;
		}
		Vector nextMoves = current.getChildren();
		int sz = nextMoves.size();
		for(int i = 0; i < sz; ++i) {
			Node n = (Node)nextMoves.elementAt(i);
			MoveAction moveAction = (MoveAction)n.getAction();
			if ((moveAction.isPass() && pt == null) || (!moveAction.isPass() && moveAction.getPoint().equals(pt))) {				
				moveAction.apply(goban);
				if (n.getChildren().size() > 0) {
					current = getRandomChild(n);					
					current.getAction().apply(goban);					
				} else {
					current = n;
				}
				if (current.getChildren().size() > 0) {
					state = STATE_SOLVING;
				} else {
					state = current.isRight() ? STATE_SOLVED : STATE_WRONG;
				}
				return;
			}				
		}
		Node n = new Node(current, false);
		MoveAction action = new MoveAction(new Point(pt), problem.playerColor);
		n.setAction(action);
		action.apply(goban);
		current = n;
		state = STATE_WRONG;
	}
	
	public Point getLastMove() {
		NodeAction action = current.getAction(); 
		if (action != null && action instanceof MoveAction) {
			return ((MoveAction)action).getPoint();
		}
		return null;
	}

	public void undo() throws GoAwayException {
		if (current.getParent() == null) {
			return;
		}
		current.getAction().undo(goban);
		current = current.getParent();
		if (current.getParent() != null) {
			MoveAction action = (MoveAction)current.getAction();
			if (action.getColor() == problem.playerColor) {
				action.undo(goban);
				current = current.getParent();
			}
		}
		state = STATE_SOLVING;
	}
	
	private Node getRandomChild(Node n) throws GoAwayException {
		Vector children = n.getChildren();
		int sz = children.size();
		if (n.isRight()) {
			int correctCnt = 0;
			for (int i = 0; i < sz; ++i) {
				if (((Node)children.elementAt(i)).isRight()) {
					++correctCnt;
				}
			}			
			int idx = random(correctCnt);
			for (int i = 0; i < sz; ++i) {
				Node m = (Node)children.elementAt(i);
				if (m.isRight()) {
					if (idx == 0) {
						return m;
					}					
					--idx;
				}
			}
			throw new GoAwayException("Could not find correct continuation");
		} else {
			// by default we use first given continuation
			return (Node)children.elementAt(0); 
		}
	}
	
	public Goban getGoban() {
		return goban;
	}
	
	public Node getCurrentNode() {
		return current; 
	}
	
	public byte getState() {
		return state;
	}
	
	private int random(int cap) {
		return Math.abs(rand.nextInt())%cap;
	}
}

package ru.goproject.goaway.common;

import java.util.Vector;

public class Node {
	private final static int STATUS_ALL		= 0xFFFFFFFF;
	private final static int STATUS_RIGHT	= 0x00000001;
	public final static int STATUS_FORCE	= 0x00000002;
	public final static int STATUS_CHOICE	= 0x00000004;
	public final static int STATUS_NOTTHIS	= 0x00000008;
	private int status;
	private String comment;
	private Node parent;
	private Vector children;
	private Vector labels;
	private NodeAction action;
	
	public NodeAction getAction() {
		return action;
	}

	public void setAction(NodeAction action) {
		this.action = action;
	}

	public String getComment() {
		return comment;
	}
	
	public void setComment(String val) {
		comment = val;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public Vector getChildren() {
		return children;
	}
	
	public Vector getLabels() {
		return labels;
	}
	
	public void setLabels(Vector val) {
		labels = val;
	}
	
	public boolean checkStatus(int flag) {
		return (status & flag) == flag;
	}
	
	private void markStatus(int flag) {
		status |= flag;
	}
	
	private void removeStatus(int flag) {	
		status &= flag^STATUS_ALL;
	}	
	
	public void markRight() {
		markStatus(STATUS_RIGHT);
	}
	
	public void markWrong() {
		removeStatus(STATUS_RIGHT);
	}
	
	public boolean isRight() {
		return checkStatus(STATUS_RIGHT);
	}
	
	public void markForce() {
		markStatus(STATUS_FORCE);
	}
	
	public void markChoice() {
		markStatus(STATUS_CHOICE);
	}
	
	public void markNotThis() {
		markStatus(STATUS_NOTTHIS);
	}

	public Node(Node parent, boolean addToParent) {
		this.parent = parent;
		children = new Vector();
		if (parent != null && addToParent) {
			parent.addChild(this);
		}
		status = 0;
	}
	
	public void addChild(Node child) {
		children.addElement(child);
	}
}

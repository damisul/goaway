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

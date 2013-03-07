/**
	Copyright (c) 2008-2013 Damir Sultanbekov All Rights Reserved. 
	This file is part of GoAway project.

    GoAway is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GoAway is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GoAway.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.goproject.goaway.collection;

public abstract class ProblemsCollection {
	protected ProblemsCollectionEventListener eventListener;
	
	public void setEventListener(ProblemsCollectionEventListener eventListener) {
		this.eventListener = eventListener;
	}
	
	public abstract void requestProblem(int problemIndex);
	public abstract int size();
	public abstract String getProblemTitle(int problemIndex);
	public abstract void refresh();
}

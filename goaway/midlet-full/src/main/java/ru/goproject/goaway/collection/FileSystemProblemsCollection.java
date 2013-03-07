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
package ru.goproject.goaway.collection;

import ru.goproject.goaway.common.Problem;

public abstract class FileSystemProblemsCollection extends ProblemsCollection {
	public final static int TYPE_FOLDER = 0;
	public final static int TYPE_SINGLEFILE = 1;
	
	protected String path;

	public void refresh() {
		clear();
		Thread t = new Thread() {
			public void run() {
				try {
					loadCollectionContents();
					eventListener.onCollectionLoaded();
				} catch (Exception e) {
					clear();
					eventListener.onProblemLoadingFailed(e);
				}
			}
		};
		t.start();
	}
	
	public void requestProblem(final int problemIndex) {
		Thread t = new Thread() {
			public void run() {
				Problem p = null;
				try {
					p = loadProblem(problemIndex);
					p.setTitle(getProblemTitle(problemIndex));
				} catch (Exception e) {
					e.printStackTrace();
					eventListener.onProblemLoadingFailed(e);
				}
				if (p != null) {
					eventListener.onProblemLoaded(p, problemIndex);
				}
			}
		};
		t.start();
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	protected abstract void loadCollectionContents() throws Exception;

	protected abstract Problem loadProblem(int index) throws Exception;
	
	public abstract int getType();
	
	protected abstract void clear();
}

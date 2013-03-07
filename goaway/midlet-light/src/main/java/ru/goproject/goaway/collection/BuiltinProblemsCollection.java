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

import java.io.InputStream;
import java.util.Vector;

import ru.goproject.goaway.common.Problem;
import ru.goproject.goaway.midlet.MidletUtils;
import ru.goproject.goaway.midlet.StringReader;
import ru.goproject.goaway.sgf.ProblemReader;

public class BuiltinProblemsCollection extends ProblemsCollection {
	private final static String PROBLEM_DIR = "/problems/";
	private final static String PROBLEM_INDEX = "/problem_index";
	private final Vector contents = new Vector();
	
	public int size() {
		return contents.size();
	}

	public String getProblemTitle(int problemIndex) {
		return (String)contents.elementAt(problemIndex);
	}

	public void requestProblem(final int problemIndex) {
		Thread t = new Thread() {
			public void run() {
				InputStream stream = null;
				try {
					stream = getClass().getResourceAsStream(PROBLEM_DIR + contents.elementAt(problemIndex));
					Problem p = ProblemReader.readProblem(stream);
					eventListener.onProblemLoaded(p, problemIndex);
				} catch (Exception e) {
					eventListener.onProblemLoadingFailed(e);
				} finally {
					MidletUtils.closeQuietly(stream);
				}
			}
		};
		t.start();
	}

	public void refresh() {
		if (contents.size() > 0) {
			// built-in collection couldn't be changed
			// so no need to re-read its content
			eventListener.onCollectionLoaded();
			return;
		}
		Thread t = new Thread() {
			public void run() {
				try {
					StringReader reader = new StringReader(getClass().getResourceAsStream(PROBLEM_INDEX));
					while (!reader.isEof()) {
						String st = reader.readLine().trim();
						if (st.length() > 0) {
							contents.addElement(st);
						}
					}
					eventListener.onCollectionLoaded();
				} catch (Exception e) {
					eventListener.onCollectionLoadingFailed(e);
				}
			}
		};
		t.start();
	}
}

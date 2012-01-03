package ru.goproject.goaway.collection;

import java.io.InputStream;
import java.util.Vector;

import ru.goproject.goaway.common.Problem;
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

	public void requestProblem(int problemIndex) {
		InputStream stream = null;
		try {
			stream = getClass().getResourceAsStream(PROBLEM_DIR + contents.elementAt(problemIndex));
			Problem p = ProblemReader.readProblem(stream);
			eventListener.onProblemLoaded(p, problemIndex);
		} catch (Exception e) {
			eventListener.onProblemLoadingFailed(e);
		}
	}

	public void refresh() {
		if (contents.size() > 0) {
			// built-in collection couldn't be changed
			// so no need to re-read its content
			eventListener.onCollectionLoaded();
			return;
		}
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
}

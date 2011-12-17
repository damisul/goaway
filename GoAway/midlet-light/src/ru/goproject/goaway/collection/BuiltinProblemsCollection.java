package ru.goproject.goaway.collection;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import ru.goproject.goaway.common.Problem;
import ru.goproject.goaway.midlet.StringReader;
import ru.goproject.goaway.sgf.ProblemReader;

public class BuiltinProblemsCollection extends ProblemsCollection {
	private final static String PROBLEM_DIR = "/problems/";
	private final static String PROBLEM_INDEX = "/problem_index";
	private Vector contents;
	
	public BuiltinProblemsCollection() throws UnsupportedEncodingException {
		contents = new Vector();
		StringReader reader = new StringReader(getClass().getResourceAsStream(PROBLEM_INDEX));
		while (!reader.isEof()) {
			String st = reader.readLine().trim();
			if (st.length() > 0) {
				contents.addElement(st);
			}
		}
	}
	
	public int size() {
		return contents.size();
	}

	protected void clear() {
		contents.removeAllElements();
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
}

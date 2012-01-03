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

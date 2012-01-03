package ru.goproject.goaway.collection;

import ru.goproject.goaway.common.Problem;

public abstract class FileSystemProblemsCollection extends ProblemsCollection {
	protected boolean refreshNeeded = true;

	public boolean isRefreshNeeded() {
		return refreshNeeded;
	}
	
	public void refresh() {
		currentIndex = 0;
		clear();
		Thread t = new Thread() {
			public void run() {
				try {
					loadCollectionContents();
					refreshNeeded = false;
				} catch (Exception e) {
					e.printStackTrace();
					clear();
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

	protected abstract void loadCollectionContents() throws Exception;

	protected abstract Problem loadProblem(int index) throws Exception;

}

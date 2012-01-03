package ru.goproject.goaway.collection;


public abstract class ProblemsCollection {
	protected ProblemsCollectionEventListener eventListener;
	protected int currentIndex;
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void setEventListener(ProblemsCollectionEventListener eventListener) {
		this.eventListener = eventListener;
	}
	
	public abstract void requestProblem(int problemIndex);
	public abstract int size();
	public abstract String getProblemTitle(int problemIndex);
	public abstract void refresh();
	
}

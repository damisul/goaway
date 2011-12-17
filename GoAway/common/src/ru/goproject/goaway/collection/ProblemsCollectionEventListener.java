package ru.goproject.goaway.collection;

import ru.goproject.goaway.common.Problem;

public interface ProblemsCollectionEventListener {
	void onProblemLoaded(Problem p, int index);
	void onProblemLoadingFailed(Exception e);
}

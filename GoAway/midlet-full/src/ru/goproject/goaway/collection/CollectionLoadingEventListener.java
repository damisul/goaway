package ru.goproject.goaway.collection;

public interface CollectionLoadingEventListener {
	void onCollectionLoadingComplete(int size);
	void onCollectionLoadingFailed(Exception e);
	void onCollectionPartLoaded(int currentSize);
}

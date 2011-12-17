package ru.goproject.goaway.util;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import ru.goproject.goaway.collection.CollectionLoadingEventListener;
import ru.goproject.goaway.midlet.LocalizedStrings;
import ru.goproject.goaway.midlet.MainForm;
import ru.goproject.goaway.midlet.MidletUtils;

public class CollectionLoadingForm extends Form implements CollectionLoadingEventListener {
	private final static String RES_TITLE = "LoadingForm.title";
	private final static String RES_PROBLEMS_FOUND = "LoadingForm.problemsFound";
	private final static String RES_COLLECTION_ERROR = "LoadingForm.collectionError";
	private final static String RES_EMPTY_COLLECTION = "LoadingForm.emptyCollection";	
	
	private StringItem itemStatus;
	private MainForm mainForm;
	
	public CollectionLoadingForm(MainForm mainForm) {
		super(LocalizedStrings.getResource(RES_TITLE));
		itemStatus = new StringItem("", null);
		this.mainForm = mainForm;
		append(itemStatus);
	}

	public void onCollectionLoadingComplete(int size) {
		if (size == 0) {
			MidletUtils.show(mainForm);
			MidletUtils.showLocalizedMessage(RES_COLLECTION_ERROR, RES_EMPTY_COLLECTION);
		} else {
			MidletUtils.show(mainForm);
		}
	}

	public void onCollectionLoadingFailed(Exception e) {
		MidletUtils.show(mainForm);
		MidletUtils.showError(e);		
	}

	public void onCollectionPartLoaded(int currentSize) {
		itemStatus.setLabel(LocalizedStrings.getResource(RES_PROBLEMS_FOUND) + currentSize);
	}
}

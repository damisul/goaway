package ru.goproject.goaway.light;

import javax.microedition.rms.RecordStore;

import ru.goproject.goaway.collection.BuiltinProblemsCollection;
import ru.goproject.goaway.collection.ProblemsCollection;
import ru.goproject.goaway.midlet.AbstractMainForm;
import ru.goproject.goaway.midlet.AbstractMidlet;

public class LightMainForm extends AbstractMainForm {

	public LightMainForm(AbstractMidlet midlet) {
		super(midlet);
	}

	protected ProblemsCollection restoreCollection(RecordStore rs) {
		return new BuiltinProblemsCollection();
	}
}

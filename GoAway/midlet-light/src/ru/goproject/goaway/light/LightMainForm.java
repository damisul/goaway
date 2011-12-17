package ru.goproject.goaway.light;

import ru.goproject.goaway.collection.BuiltinProblemsCollection;
import ru.goproject.goaway.midlet.AbstractMainForm;
import ru.goproject.goaway.midlet.AbstractMidlet;
import ru.goproject.goaway.midlet.MidletUtils;

public class LightMainForm extends AbstractMainForm {

	public LightMainForm(AbstractMidlet midlet) {
		super(midlet);
		try {
			collection = new BuiltinProblemsCollection();
		} catch (Exception e) {
			MidletUtils.showError(e);
		}
		collection.setEventListener(canvas);
		canvas.setProblemsCollection(collection);
		Settings settings = Settings.getInstance();
		canvas.setProblemIndex(settings.getProblemIndex());		
		MidletUtils.show(this);
	}

}

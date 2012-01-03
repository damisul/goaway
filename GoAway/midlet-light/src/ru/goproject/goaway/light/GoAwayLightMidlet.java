package ru.goproject.goaway.light;

import ru.goproject.goaway.midlet.AbstractMainForm;
import ru.goproject.goaway.midlet.AbstractMidlet;

public class GoAwayLightMidlet extends AbstractMidlet {
	protected AbstractMainForm createMainForm() {
		return new LightMainForm(this);
	}

	protected String[] getLocalizationBasenames() {
		return new String[] {"i18n"};
	}
}

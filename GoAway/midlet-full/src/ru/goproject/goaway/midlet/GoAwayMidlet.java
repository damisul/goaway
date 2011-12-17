package ru.goproject.goaway.midlet;

public class GoAwayMidlet extends AbstractMidlet {
	protected AbstractMainForm createMainForm() {
		return new MainForm(this);
	}

	protected void saveSettings() {
		Settings settings = Settings.getInstance();
		settings.setProblemIndex(mainForm.getProblemIndex());
		Settings.saveSettings();
	}

	protected String[] getLocalizationBasenames() {
		return new String[] {"i18n", "midlet"};
	}
}
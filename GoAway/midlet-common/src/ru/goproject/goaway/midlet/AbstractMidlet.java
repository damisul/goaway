package ru.goproject.goaway.midlet;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public abstract class AbstractMidlet extends MIDlet {
	protected AbstractMainForm mainForm;
	public AbstractMidlet() {
		MidletUtils.init(this);
	}	

	protected final void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		saveSettings();
	}
	
	protected final void pauseApp() {
		saveSettings();
	}

	protected final void startApp() throws MIDletStateChangeException {		
		if (mainForm == null) {
			String locale = System.getProperty ("microedition.locale");
			System.out.println("Current locale: " + locale);
			if (locale != null) {
				locale = locale.substring(0, 2);
			} else {
				locale = MidletSettings.DEFAULT_LANG;
			}
			try {
				LocalizedStrings.getInstance().loadStrings(locale, getLocalizationBasenames());
				mainForm = createMainForm();
			} catch (Exception e) {
				MidletUtils.showError(e);
			}
		}
	}
	
	protected abstract AbstractMainForm createMainForm();
	
	protected abstract String[] getLocalizationBasenames();
	
	protected abstract void saveSettings();
}

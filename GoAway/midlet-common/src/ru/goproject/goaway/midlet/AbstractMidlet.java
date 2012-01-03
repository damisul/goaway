package ru.goproject.goaway.midlet;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public abstract class AbstractMidlet extends MIDlet {
	private final static String DEFAULT_LANG = "en";	
	
	protected AbstractMainForm mainForm;
	public AbstractMidlet() {
		MidletUtils.init(this);
	}	

	protected final void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		mainForm.saveSettings();
	}
	
	protected final void pauseApp() {
		mainForm.saveSettings();
	}

	protected final void startApp() throws MIDletStateChangeException {		
		if (mainForm == null) {
			String locale = System.getProperty ("microedition.locale");
			System.out.println("Current locale: " + locale);
			if (locale != null) {
				locale = locale.substring(0, 2);
			} else {
				locale = DEFAULT_LANG;
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
}

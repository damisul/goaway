/**
 * Copyright (c) 2008-2013 Damir Sultanbekov All Rights Reserved. 
 * This file is part of GoAway project.
 * 
 * GoAway is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GoAway is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GoAway.  If not, see <http://www.gnu.org/licenses/>.
 */
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

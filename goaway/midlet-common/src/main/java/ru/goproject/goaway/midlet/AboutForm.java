/**
 * Copyright (c) 2013 Damir Sultanbekov All Rights Reserved. 
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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 * 'About' form displays copyright and license information
 */
public class AboutForm extends Form implements CommandListener {
	public final static String RES_TITLE = "AboutForm.title";
	private final static String RES_COPYRIGHT = "AboutForm.copyright";
	private final static String RES_HOMEPAGE = "AboutForm.homepage";
	private final static String RES_LICENSE = "AboutForm.license";
	private final static String HOMEPAGE_ADDRESS = " http://goaway.goproject.ru";
	private Command cmdOk;
	private AbstractMainForm mainForm;
	
	public AboutForm(AbstractMainForm mainForm) {
		super(LocalizedStrings.getResource(RES_TITLE) + "\n");
		this.mainForm = mainForm;		
		append(MidletUtils.getAppName() + "\n");
		append(LocalizedStrings.getResource(RES_COPYRIGHT) + "\n");
		append(LocalizedStrings.getResource(RES_HOMEPAGE) + HOMEPAGE_ADDRESS + "\n");
		append(LocalizedStrings.getResource(RES_LICENSE) + "\n");
		cmdOk = new Command(LocalizedStrings.getResource(CommonStrings.RES_OK), Command.BACK, 1);		
		addCommand(cmdOk);
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable display) {
		if (cmd == cmdOk) {
			MidletUtils.show(mainForm);
		}
	}
}

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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public class ProblemSelectorForm extends Form implements CommandListener {
	private final static String RES_TITLE = "Selector.title";
	private final static String RES_PROBLEM_INDEX_LABEL = "Selector.problemIndex";
	private final static String RES_INVALID_PROBLEM_INDEX = "Selector.invalidProblemIndex";
	private GobanCanvas parent;
	private Command cmdOk;
	private Command cmdCancel;
	private TextField itemProblemIndex;
	public ProblemSelectorForm(GobanCanvas parent) {
		super(LocalizedStrings.getResource(RES_TITLE));
		this.parent = parent;
		
		int maxSize = String.valueOf(parent.getCollectionSize()).length();
		itemProblemIndex = new TextField(LocalizedStrings.getResource(RES_PROBLEM_INDEX_LABEL), "1", maxSize, TextField.NUMERIC);
		append(itemProblemIndex);
		
		cmdOk = new Command(LocalizedStrings.getResource(CommonStrings.RES_OK), Command.OK, 1);
		cmdCancel = new Command(LocalizedStrings.getResource(CommonStrings.RES_CANCEL), Command.CANCEL, 2);
		addCommand(cmdOk);
		addCommand(cmdCancel);
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable d) {
		if (cmd == cmdOk) {
			
			boolean invalid = false;
			int problemIndex = 0;
			try {
				problemIndex = Integer.parseInt(itemProblemIndex.getString()) - 1;
				invalid = problemIndex < 0 || problemIndex >= parent.getCollectionSize(); 
			} catch (NumberFormatException e) {
				invalid = true;
			}			
			
			if (invalid) {
				MidletUtils.showLocalizedMessage(CommonStrings.RES_ERROR, RES_INVALID_PROBLEM_INDEX);
			} else {
				parent.setProblemIndex(problemIndex);
				parent.requestProblem();
				MidletUtils.show(parent);
			}
		} else if (cmd == cmdCancel) {
			MidletUtils.show(parent);
		}
	}

}

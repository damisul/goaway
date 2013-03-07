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
import javax.microedition.lcdui.StringItem;

import ru.goproject.goaway.common.Problem;

public class ProblemInfoForm extends Form implements CommandListener {
	public static final String TITLE = "Info.title";
	private static final String RES_PROBLEM_TITLE = "Info.problemTitle";
	private static final String RES_SOURCE = "Info.source";
	private static final String RES_GENRE = "Info.genre";
	private static final String RES_DIFFICULTY = "Info.difficulty";
	private Displayable parent;	
	
	public ProblemInfoForm(Displayable parent, Problem p) {
		super(LocalizedStrings.getResource(TITLE));
		this.parent = parent;
		append(new StringItem(LocalizedStrings.getResource(RES_PROBLEM_TITLE), p.getTitle()));
		append(new StringItem(LocalizedStrings.getResource(RES_SOURCE), p.getSource()));
		append(new StringItem(LocalizedStrings.getResource(RES_GENRE), p.getGenre()));
		append(new StringItem(LocalizedStrings.getResource(RES_DIFFICULTY), p.getDifficulty()));
		addCommand(new Command(LocalizedStrings.getResource(CommonStrings.RES_OK), Command.OK, 1));
		setCommandListener(this);
	}
	
	public void commandAction(Command cmd, Displayable d) {
		MidletUtils.show(parent);
	}
}

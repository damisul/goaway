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

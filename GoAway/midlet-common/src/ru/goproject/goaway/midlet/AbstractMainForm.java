package ru.goproject.goaway.midlet;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDletStateChangeException;

import ru.goproject.goaway.collection.ProblemsCollection;

public class AbstractMainForm extends Form implements CommandListener {
	public final static String RES_TITLE = "MainForm.title";	
	private final static String RES_CMD_SOLVE_PROBLEMS = "MainForm.cmdSolveProblems";
	private final static String RES_CMD_EXIT = "MainForm.cmdExit";
	private final static String RES_COPYRIGHT = "MainForm.copyright";
	private final static String RES_HOMEPAGE = "MainForm.homepage";
	private final static String HOMEPAGE_ADDRESS = " http://goaway.goproject.ru";

	private static Command cmdSolveProblems;
	private static Command cmdExit;
	
	private AbstractMidlet midlet;	
	protected GobanCanvas canvas;
	protected ProblemsCollection collection;
	
	public AbstractMainForm(AbstractMidlet midlet) {
		super(LocalizedStrings.getResource(RES_TITLE));
		this.midlet = midlet;
		
		cmdSolveProblems = new Command(LocalizedStrings.getResource(RES_CMD_SOLVE_PROBLEMS), Command.BACK, 1);
		cmdExit = new Command(LocalizedStrings.getResource(RES_CMD_EXIT), Command.STOP, 3);

		addCommand(cmdSolveProblems);
		addCommand(cmdExit);
		setCommandListener(this);
		
		canvas = new GobanCanvas(this);
		append(MidletUtils.getAppName());
		append(LocalizedStrings.getResource(RES_COPYRIGHT));
		append(LocalizedStrings.getResource(RES_HOMEPAGE) + HOMEPAGE_ADDRESS);
	}

	public void commandAction(Command cmd, Displayable d) {
		if (cmd == cmdSolveProblems) {			
			MidletUtils.show(canvas);
		} else if (cmd == cmdExit) {
			try {				 
				midlet.destroyApp(false);
				midlet.notifyDestroyed();
			} catch (MIDletStateChangeException ex) {
			}
		}
	}
	
	public int getProblemIndex() {
		return canvas.getProblemIndex();
	}	
}

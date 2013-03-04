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

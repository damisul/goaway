package ru.goproject.goaway.midlet;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import ru.goproject.goaway.collection.ProblemsCollection;

public abstract class AbstractMainForm extends Form implements CommandListener {
	public final static String RES_TITLE = "MainForm.title";	
	private final static String RES_CMD_SOLVE_PROBLEMS = "MainForm.cmdSolveProblems";
	private final static String RES_CMD_EXIT = "MainForm.cmdExit";
	private final static String RES_COPYRIGHT = "MainForm.copyright";
	private final static String RES_HOMEPAGE = "MainForm.homepage";
	private final static String HOMEPAGE_ADDRESS = " http://goaway.goproject.ru";

	private final static String RECORDSTORE_NAME = "GoAway.Settings";
	protected final static int RECORDSTORE_POS_PROBLEM_INDEX = 0;

	private static Command cmdSolveProblems;
	private static Command cmdExit;

	private AbstractMidlet midlet;	
	protected GobanCanvas canvas;
	protected ProblemsCollection collection;

	public AbstractMainForm(AbstractMidlet midlet) {
		super(LocalizedStrings.getResource(RES_TITLE));
		this.midlet = midlet;

		cmdSolveProblems = new Command(LocalizedStrings.getResource(RES_CMD_SOLVE_PROBLEMS), Command.SCREEN, 1);
		cmdExit = new Command(LocalizedStrings.getResource(RES_CMD_EXIT), Command.SCREEN, 3);

		addCommand(cmdSolveProblems);
		addCommand(cmdExit);
		setCommandListener(this);
		
		canvas = new GobanCanvas(this);
		
		RecordStore rs = null;
		try {
			rs = getSettingsStore();
			ProblemsCollection col = restoreCollection(rs);
			setProblemsCollection(col);
			readSettingsFromStore(rs);
		} catch (Exception e) {
			MidletUtils.showError(e);
		} finally {
			closeQuietly(rs);
		}
		
		append(MidletUtils.getAppName());
		append(LocalizedStrings.getResource(RES_COPYRIGHT));
		append(LocalizedStrings.getResource(RES_HOMEPAGE) + HOMEPAGE_ADDRESS);
		
		MidletUtils.show(this);
	}

	protected void setProblemsCollection(ProblemsCollection collection) {
		collection.setEventListener(canvas);
		canvas.setProblemsCollection(collection);
		this.collection = collection;
	}

	public void commandAction(Command cmd, Displayable d) {
		if (cmd == cmdSolveProblems) {
			canvas.show();
		} else if (cmd == cmdExit) {
			try {				 
				midlet.destroyApp(false);
				midlet.notifyDestroyed();
			} catch (MIDletStateChangeException ex) {
			}
		}
	}

	private RecordStore getSettingsStore() throws RecordStoreException  {
		return RecordStore.openRecordStore(RECORDSTORE_NAME, true);
	}

	private static void closeQuietly(RecordStore rs) {
		if (rs != null) {
			try {
				rs.closeRecordStore();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

	public void writeSettingsToStore(RecordStore rs) throws RecordStoreException {
		int problemIndex = collection.getCurrentIndex();
		rs.setRecord(RECORDSTORE_POS_PROBLEM_INDEX, MidletUtils.intToByteArray(problemIndex), 0, 4);
	}
	
	private void readSettingsFromStore(RecordStore rs) throws RecordStoreException {
		int problemIndex = 0;
		try {
			problemIndex = MidletUtils.byteArrayToInt(rs.getRecord(RECORDSTORE_POS_PROBLEM_INDEX));
		} catch (Exception e) {
		}
		canvas.setProblemIndex(problemIndex);
	}

	public final void saveSettings() {
		RecordStore rs = null;
		try {
			rs = getSettingsStore();
			writeSettingsToStore(rs);
		} catch (Exception e) {
		} finally {
			closeQuietly(rs);
		}
	}
	
	protected abstract ProblemsCollection restoreCollection(RecordStore rs);	
}

package ru.goproject.goaway.midlet;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import ru.goproject.goaway.collection.FileSystemProblemsCollection;
import ru.goproject.goaway.collection.FolderProblemsCollection;
import ru.goproject.goaway.collection.SingleFileProblemsCollection;

public class MainForm extends AbstractMainForm {
	private static Command cmdSettings;

	public MainForm(AbstractMidlet midlet) {		
		super(midlet);
		cmdSettings = new Command(LocalizedStrings.getResource(SettingsForm.RES_TITLE), Command.SCREEN, 2);
		addCommand(cmdSettings);			
		Settings settings = Settings.getInstance();
		applySettings(settings, true);
	}

	public void applySettings(Settings settings, boolean keepProblemIndex) {
		if (settings.getCollectionType() == Settings.COLLECTION_SINGLEFILE) {
			SingleFileProblemsCollection sfpc;
			if (collection != null && (collection instanceof SingleFileProblemsCollection)) {
				sfpc = (SingleFileProblemsCollection)collection;
			} else {
				sfpc = new SingleFileProblemsCollection();
				collection = sfpc;
			}
			sfpc.setFileName(settings.getCollectionFileName());
		} else {
			FolderProblemsCollection fpc;
			if (collection != null && (collection instanceof FolderProblemsCollection)) {
				fpc = (FolderProblemsCollection)collection;
			} else {
				fpc = new FolderProblemsCollection();
				collection = fpc;
			}
			fpc.setFolder(settings.getCollectionFolder());
		}
		
		FileSystemProblemsCollection collection = (FileSystemProblemsCollection)this.collection;
		collection.setEventListener(canvas);
		canvas.setProblemsCollection(collection);

		if (collection.isRefreshNeeded()) {
			if (keepProblemIndex) {
				canvas.setProblemIndex(settings.getProblemIndex());				
			} else {
				canvas.setProblemIndex(0);
			}
			collection.refresh();
		}
		canvas.requestProblem();
		MidletUtils.show(this);
	}

	public void commandAction(Command cmd, Displayable d) {
		if (cmd == cmdSettings) {
			SettingsForm f = new SettingsForm(this);
			MidletUtils.show(f);
		} else {
			super.commandAction(cmd, d);
		}
	}
}

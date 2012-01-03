package ru.goproject.goaway.midlet;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import ru.goproject.goaway.collection.FileSystemProblemsCollection;
import ru.goproject.goaway.collection.FolderProblemsCollection;
import ru.goproject.goaway.collection.ProblemsCollection;
import ru.goproject.goaway.collection.SingleFileProblemsCollection;

public class MainForm extends AbstractMainForm {
	private final static int RECORDSTORE_POS_COLLECTION_TYPE = 2;
	private final static int RECORDSTORE_POS_PATH = 3;
	
	private static Command cmdSettings;

	public MainForm(AbstractMidlet midlet) {		
		super(midlet);
		cmdSettings = new Command(LocalizedStrings.getResource(SettingsForm.RES_TITLE), Command.SCREEN, 2);
		addCommand(cmdSettings);			
	}

	public void commandAction(Command cmd, Displayable d) {
		if (cmd == cmdSettings) {
			SettingsForm f = new SettingsForm(this);
			MidletUtils.show(f);
		} else {
			super.commandAction(cmd, d);
		}
	}

	protected ProblemsCollection restoreCollection(RecordStore rs) {
		byte collectionType = -1;
		String path = null;
		try {
			collectionType = rs.getRecord(RECORDSTORE_POS_COLLECTION_TYPE)[0];
			path = new String(rs.getRecord(RECORDSTORE_POS_PATH));
		} catch (Exception e) {
		}
		return createCollection(collectionType, path);
	}
	
	protected ProblemsCollection createCollection(int collectionType, String path) {
		FileSystemProblemsCollection collection;
		if (collectionType == FileSystemProblemsCollection.TYPE_SINGLEFILE) {
			collection = new SingleFileProblemsCollection();
		} else {
			collection = new FolderProblemsCollection();
		}
		collection.setPath(path);
		return collection;
	}

	public int getCollectionType() {
		return ((FileSystemProblemsCollection)collection).getType();
	}

	public void setProblemsCollection(int collectionType, String path) {
		ProblemsCollection col = createCollection(collectionType, path);
		setProblemsCollection(col);
	}

	public void writeSettingsToStore(RecordStore rs) throws RecordStoreException {
		super.writeSettingsToStore(rs);
		FileSystemProblemsCollection col = (FileSystemProblemsCollection)collection;
		rs.setRecord(RECORDSTORE_POS_COLLECTION_TYPE, new byte[] { (byte)col.getType() }, 0, 1);
		byte[] data = col.getPath() == null ? new byte[] {} : col.getPath().getBytes();
		rs.setRecord(RECORDSTORE_POS_PATH, data, 0, data.length);
	}
}

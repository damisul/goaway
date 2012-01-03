package ru.goproject.goaway.midlet;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;

import ru.goproject.goaway.util.FileSystemChooser;


public class SettingsForm extends Form implements CommandListener, ItemStateListener {
	public final static String RES_TITLE = "Settings.title";
	private final static String RES_COLLECTION_FOLDER = "Settings.collectionFolder";
	private final static String RES_COLLECTION_FILENAME = "Settings.collectionFileName";
	private final static String RES_COLLECTION_TYPE = "Settings.collectionType";
	private final static String RES_COLLECTION_TYPE_SINGLEFILE = "Settings.collectionTypeSingleFile";
	private final static String RES_COLLECTION_TYPE_FOLDER = "Settings.collectionTypeFolder";
	private final static String RES_CMD_CHANGE_PATH = "Settings.cmdChangePath";
	private MainForm mainForm;
	private ChoiceGroup itemCollectionType;	
	private StringItem itemCollectionPath;
	private Command cmdChangePath;	
	private Command cmdOk;	
	
	public SettingsForm(MainForm mainForm) {
		super(LocalizedStrings.getResource(RES_TITLE));
		this.mainForm = mainForm;
		
		Settings settings = Settings.getInstance();
		
		itemCollectionType = new ChoiceGroup(LocalizedStrings.getResource(RES_COLLECTION_TYPE), Choice.EXCLUSIVE);
		itemCollectionType.append(LocalizedStrings.getResource(RES_COLLECTION_TYPE_FOLDER), null);
		itemCollectionType.append(LocalizedStrings.getResource(RES_COLLECTION_TYPE_SINGLEFILE), null);
		itemCollectionType.setSelectedIndex(settings.getCollectionType(), true);
		append(itemCollectionType);

		itemCollectionPath = new StringItem(null, null);
		append(itemCollectionPath);
		
		itemStateChanged(itemCollectionType);
		
		
		cmdOk = new Command(LocalizedStrings.getResource(CommonStrings.RES_OK), Command.BACK, 1);
		addCommand(cmdOk);
		cmdChangePath = new Command(LocalizedStrings.getResource(RES_CMD_CHANGE_PATH), Command.SCREEN, 1);
		addCommand(cmdChangePath);
		
		setCommandListener(this);
		setItemStateListener(this);
	}

	public void changePath(String path) {
		itemCollectionPath.setText(path);
		MidletUtils.show(this);
	}
	
	
	public void commandAction(Command cmd, Displayable d) {
		if (cmd == cmdOk) {
			Settings settings = Settings.getInstance();
			if (itemCollectionType.getSelectedIndex() == Settings.COLLECTION_FOLDER) {
				settings.setCollectionFolder(itemCollectionPath.getText());	
			} else {
				settings.setCollectionFileName(itemCollectionPath.getText());	
			}
			settings.setCollectionType((byte)itemCollectionType.getSelectedIndex());
			mainForm.applySettings(settings, false);
		} else if (cmd == cmdChangePath) {
			FileSystemChooser fc;
			if (itemCollectionType.getSelectedIndex() == Settings.COLLECTION_FOLDER) {
				fc = new FileSystemChooser(this, cmd.getLabel(), null);			
			} else {
				fc = new FileSystemChooser(this, cmd.getLabel(), "*.sgf");	
			}
			MidletUtils.show(fc);
		}

	}

	public void itemStateChanged(Item item) {
		Settings settings = Settings.getInstance();
		if (item == itemCollectionType) {
			if (itemCollectionType.getSelectedIndex() == Settings.COLLECTION_FOLDER) {
				itemCollectionPath.setLabel(LocalizedStrings.getResource(RES_COLLECTION_FOLDER));
				itemCollectionPath.setText(settings.getCollectionFolder());
			} else {
				itemCollectionPath.setLabel(LocalizedStrings.getResource(RES_COLLECTION_FILENAME));
				itemCollectionPath.setText(settings.getCollectionFileName());				
			}
		}
	}
}
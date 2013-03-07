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

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;

import ru.goproject.goaway.collection.FileSystemProblemsCollection;
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

		itemCollectionType = new ChoiceGroup(LocalizedStrings.getResource(RES_COLLECTION_TYPE), Choice.EXCLUSIVE);
		itemCollectionType.append(LocalizedStrings.getResource(RES_COLLECTION_TYPE_FOLDER), null);
		itemCollectionType.append(LocalizedStrings.getResource(RES_COLLECTION_TYPE_SINGLEFILE), null);
		itemCollectionType.setSelectedIndex(mainForm.getCollectionType(), true);
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
			int type = itemCollectionType.getSelectedIndex();
			String path = itemCollectionPath.getText();
			mainForm.setProblemsCollection(type, path);
			MidletUtils.show(mainForm);
		} else if (cmd == cmdChangePath) {
			FileSystemChooser fc;
			if (itemCollectionType.getSelectedIndex() == FileSystemProblemsCollection.TYPE_FOLDER) {
				fc = new FileSystemChooser(this, cmd.getLabel(), null);			
			} else {
				fc = new FileSystemChooser(this, cmd.getLabel(), "*.sgf");	
			}
			MidletUtils.show(fc);
		}

	}

	public void itemStateChanged(Item item) {
		if (item == itemCollectionType) {
			if (itemCollectionType.getSelectedIndex() == FileSystemProblemsCollection.TYPE_FOLDER) {
				itemCollectionPath.setLabel(LocalizedStrings.getResource(RES_COLLECTION_FOLDER));
				itemCollectionPath.setText("");
			} else {
				itemCollectionPath.setLabel(LocalizedStrings.getResource(RES_COLLECTION_FILENAME));
				itemCollectionPath.setText("");				
			}
		}
	}
}
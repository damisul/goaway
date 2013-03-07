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
package ru.goproject.goaway.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import ru.goproject.goaway.midlet.CommonStrings;
import ru.goproject.goaway.midlet.FileSystemUtils;
import ru.goproject.goaway.midlet.LocalizedStrings;
import ru.goproject.goaway.midlet.MidletUtils;
import ru.goproject.goaway.midlet.SettingsForm;

public class FileSystemChooser extends List implements CommandListener {	
	private final static String FOLDER_UP = "..";
	private final static String folderSeparator = "/";//FileSystemUtils.getFileSeparator(); 
	private final static String ROOT = "file:///";
	
	private final static String RES_CMD_OPEN = "Settings.cmdOpenFolder";	
	
	private SettingsForm settingsForm;
	private Command cmdOk;
	private Command cmdCancel;
	private Command cmdOpen;
	private String fileMask;
	private Vector pathEntries;

	public FileSystemChooser(SettingsForm settingsForm, String title, String fileMask) {		
		super(title, Choice.IMPLICIT);
		this.settingsForm = settingsForm;
		this.fileMask = fileMask;
		setCommandListener(this);
		cmdOk = new Command(LocalizedStrings.getResource(CommonStrings.RES_OK), Command.OK, 1);
		cmdCancel = new Command(LocalizedStrings.getResource(CommonStrings.RES_CANCEL), Command.BACK, 2);
		cmdOpen = new Command(LocalizedStrings.getResource(RES_CMD_OPEN), Command.ITEM, 3);
		pathEntries = new Vector();
		addCommand(cmdOk);
		addCommand(cmdCancel);
		addCommand(cmdOpen);
		setSelectCommand(cmdOpen);
		loadList(ROOT);
	}
	
	private String getCurrentFolder() {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < pathEntries.size(); ++i) {
			res.append(pathEntries.elementAt(i));
		}
		return res.toString();
	}
	
	private void loadList(final String path) {
		deleteAll();		
		if (path.equals(FOLDER_UP)) {
			pathEntries.removeElementAt(pathEntries.size() - 1);			
		} else {
			pathEntries.addElement(path);
		}
		
		final String currentFolder = getCurrentFolder();
		Thread loadThread = null;
		if (currentFolder.equals(ROOT)) {			
			loadThread = new Thread() {
				public void run() {
					Enumeration folders = FileSystemRegistry.listRoots();
					while (folders.hasMoreElements()) {
						String s = (String)folders.nextElement();
						if (!s.endsWith(folderSeparator)) {
							s += folderSeparator;
						}
						append(s, null);
					}
				}				
			};			
		} else {
			loadThread = new Thread() {
				public void run() {
					FileConnection fc = null;
					try {
						append(FOLDER_UP, null);
						fc = (FileConnection)Connector.open(currentFolder, Connector.READ);
						Enumeration files = fc.list();
						while (files.hasMoreElements()) {							
							String s = (String)files.nextElement();
							if (s.endsWith(folderSeparator)) {
								append(s, null);
							}
						}
						if (fileMask != null) {
							files = fc.list(fileMask, false);
							while (files.hasMoreElements()) {
								String s = (String)files.nextElement();
								append(s, null);
							}
						}
					} catch (IOException e) {
						MidletUtils.showError(e);
					} finally {
						FileSystemUtils.closeFileConnection(fc);
					}
				}				
			};
		}
		loadThread.start();
	}

	public void commandAction(Command cmd, Displayable displayable) {
		if (cmd == cmdOk) {
			String currentFolder = getCurrentFolder();
			String selectedItem = getString(getSelectedIndex());

			if (fileMask != null) {
				if (selectedItem.equals(FOLDER_UP) || selectedItem.endsWith(folderSeparator)) {
					return;
				} else {
					settingsForm.changePath(currentFolder + selectedItem);	
				}
			} else {
				if (selectedItem.equals(FOLDER_UP)) {
					settingsForm.changePath(currentFolder);
				} else {
					settingsForm.changePath(currentFolder + selectedItem);
				}
			}
		} else if (cmd == cmdOpen) {
			String item = getString(getSelectedIndex());
			if (item.equals(FOLDER_UP) || item.endsWith(folderSeparator)) {
				loadList(item);
			}
		} else if (cmd == cmdCancel) {
			MidletUtils.show(settingsForm);
		}
	}
}
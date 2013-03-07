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
package ru.goproject.goaway.collection;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import ru.goproject.goaway.common.Problem;
import ru.goproject.goaway.midlet.FileSystemUtils;
import ru.goproject.goaway.sgf.ProblemReader;


public class FolderProblemsCollection extends FileSystemProblemsCollection {
	private final Vector files = new Vector();

	protected void clear() {
		files.removeAllElements();
	}
	
	public int size() {
		return files.size();
	}

	protected void loadCollectionContents() throws Exception {
		if (path == null || "".equals(path)) {
			return;
		}
		FileConnection fc = null;
		try {
			fc = (FileConnection)Connector.open(path, Connector.READ);
			Enumeration list = fc.list("*.*", false);
			while (list.hasMoreElements()) {
				String fileName = (String)list.nextElement();
				if (fileName.toLowerCase().endsWith(".sgf")) {
					files.addElement(fileName);
				}
			}
		} finally {
			FileSystemUtils.closeFileConnection(fc);
		}
	}

	public String getProblemTitle(int problemIndex) {
		return (String)files.elementAt(problemIndex);
	}

	protected Problem loadProblem(int index) throws Exception {
		FileConnection fc = null;
		String fileName = (String)files.elementAt(index);
		InputStream stream = null;
		try {
			fc =(FileConnection) Connector.open(path + fileName, Connector.READ);
			stream = fc.openInputStream();
			return ProblemReader.readProblem(stream);
		} finally {
			if (stream != null) {
				stream.close();	
			}
			FileSystemUtils.closeFileConnection(fc);
		}
	}

	public int getType() {
		return TYPE_FOLDER;
	}
}

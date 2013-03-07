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
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import ru.goproject.goaway.common.Problem;
import ru.goproject.goaway.exception.GoAwayException;
import ru.goproject.goaway.midlet.FileSystemUtils;
import ru.goproject.goaway.midlet.MidletUtils;
import ru.goproject.goaway.sgf.ProblemReader;
import ru.goproject.goaway.util.FilePos;
import ru.goproject.goaway.util.SmartReader;

public class SingleFileProblemsCollection extends FileSystemProblemsCollection {
	private final Vector files = new Vector();
	private String shortFileName;

	protected void clear() {
		files.removeAllElements();		
	}

	public int size() {
		return files.size();
	}
	
	public String getProblemTitle(int problemIndex) {
		return shortFileName + " [" + Integer.toString(problemIndex + 1) + "/" + size() + "]";
	}

	protected void loadCollectionContents() throws Exception {
		FileConnection fc = null;
		if ("".equals(path)) {
			return;
		}
		
		InputStream stream = null;
		try {
			fc = (FileConnection)Connector.open(path, Connector.READ);
			stream = fc.openInputStream();
			SmartReader reader = new SmartReader(stream, ProblemReader.RAW_ENCODING);
			// Перед первой задачей может идти заголовок коллекции, пропускаем его
			reader.readTill('(');

			FilePos filePos = reader.getFilePos();
			int braceCount = 0;
			while (!reader.isEof()) {
				do {
					char c = reader.getCurrentChar();
					switch (c) {
						case '(':
							++braceCount;
							break;
						case ')':
							--braceCount;
							break;
						case '[':
							reader.readTill(']', '\\');
							break;
					}
					reader.next();
				} while (braceCount != 0 && reader.isReady());
				if (braceCount > 0) {
					throw new GoAwayException("Unexpected end of file: ')' expected");
				}
				files.addElement(filePos);
				reader.skipBlanks();
				if (reader.isReady()) {
					filePos = reader.getFilePos();
					reader.read('(');
					++braceCount;
				}
			}
			reader.close();
		} finally {
			MidletUtils.closeQuietly(stream);
			FileSystemUtils.closeFileConnection(fc);
		}
	}

	protected Problem loadProblem(int index) throws Exception {
		FileConnection fc = null;
		try {
			fc = (FileConnection)Connector.open(path, Connector.READ);
			shortFileName = fc.getName();
			System.out.println(shortFileName);
			FilePos filePos = (FilePos)files.elementAt(index);
			SmartReader reader = new SmartReader(fc.openInputStream(), ProblemReader.RAW_ENCODING, filePos);
			ProblemReader problemReader = new ProblemReader();
			Problem p = problemReader.read(reader);
			reader.close();
			return p; 
		} finally {
			FileSystemUtils.closeFileConnection(fc);
		}		
	}

	public int getType() {
		return TYPE_SINGLEFILE;
	}

}

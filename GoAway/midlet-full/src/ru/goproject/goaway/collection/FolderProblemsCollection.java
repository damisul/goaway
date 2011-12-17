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
	private String folder;
	private Vector files = new Vector();
	public String getFolder() {
		return folder;
	}

	public void setFolder(final String folder) {
		if (!folder.equals(this.folder)) {
			this.folder = folder;
			refreshNeeded = true;
		}		
	}

	public int size() {
		return files.size();
	}

	protected void loadCollectionContents() throws Exception {
		if (folder == null || "".equals(folder)) {
			return;
		}
		FileConnection fc = null;
		try {
			fc = (FileConnection)Connector.open(folder, Connector.READ);
			Enumeration list = fc.list("*.*", false);
			int i = 0;
			while (list.hasMoreElements()) {
				String fileName = (String)list.nextElement();
				if (fileName.toLowerCase().endsWith(".sgf")) {
					++i;
					files.addElement(fileName);
					if (i % LOADING_NOTIFICTION_STEP == 0) {
						loadingListener.onCollectionPartLoaded(i);
					}
				}
			}			
		} finally {
			FileSystemUtils.closeFileConnection(fc);
		}
	}

	protected void clear() {
		files.removeAllElements();
	}

	public String getProblemTitle(int problemIndex) {
		return (String)files.elementAt(problemIndex);
	}

	protected Problem loadProblem(int index) throws Exception {
		FileConnection fc = null;
		String fileName = (String)files.elementAt(index);
		InputStream stream = null;
		try {
			fc =(FileConnection) Connector.open(folder + fileName, Connector.READ);
			stream = fc.openInputStream();
			return ProblemReader.readProblem(stream);
		} finally {
			if (stream != null) {
				stream.close();	
			}
			FileSystemUtils.closeFileConnection(fc);
		}
	}
}

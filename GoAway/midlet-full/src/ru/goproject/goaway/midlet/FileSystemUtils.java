package ru.goproject.goaway.midlet;

import java.io.IOException;

import javax.microedition.io.file.FileConnection;

public class FileSystemUtils {
	public static void closeFileConnection(FileConnection fc) {
		if (fc != null) {
			try {
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
}

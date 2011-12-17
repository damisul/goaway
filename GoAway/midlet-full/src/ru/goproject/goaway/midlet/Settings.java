package ru.goproject.goaway.midlet;

import javax.microedition.rms.RecordStore;

public class Settings {
	public final static byte COLLECTION_FOLDER = 0;
	public final static byte COLLECTION_SINGLEFILE = 1;

	private final static String RECORDSTORE_NAME = "GoAway.Midlet.Settings";
	
	private final static int POS_COLLECTION_TYPE = 1;
	private final static int POS_FOLDER = 2;
	private final static int POS_FILENAME = 3;
	private final static int POS_PROBLEM_INDEX = 4;
	
	private byte collectionType; 
	private String collectionFolder;
	private String collectionFileName;
	private int problemIndex;	
	
	private static Settings instance;
	private Settings() {		
	}
	
	public static Settings getInstance() {
		if (instance == null) {
			readSettings();
		}
		return instance;
	}
	
	public static void saveSettings() {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(RECORDSTORE_NAME, false);
			rs.setRecord(POS_COLLECTION_TYPE, new byte[] { instance.getCollectionType() }, 0, 1);
			byte[] data = instance.getCollectionFolder().getBytes();
			rs.setRecord(POS_FOLDER, data, 0, data.length);
			data = instance.getCollectionFileName().getBytes();
			rs.setRecord(POS_FILENAME, data, 0, data.length);
			data = Integer.toString(instance.problemIndex).getBytes();
			rs.setRecord(POS_PROBLEM_INDEX, data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs);
		}
	}
	
	private static void close(RecordStore rs) {
		if (rs != null) {
			try {
				rs.closeRecordStore();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	private static void initNewRecordStore() {
		RecordStore rs = null;
		try {
			RecordStore.deleteRecordStore(RECORDSTORE_NAME);
		} catch (Exception e) {
			
		}
		try {
			rs = RecordStore.openRecordStore(RECORDSTORE_NAME, true);
			rs.addRecord(new byte[] {COLLECTION_FOLDER}, 0, 1);
			byte[] data = "".getBytes(); 
			rs.addRecord(data, 0, data.length);
			rs.addRecord(data, 0, data.length);
			data = "0".getBytes();
			rs.addRecord(data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs);
		}
	}
	
	public static Settings readSettings() {
		instance = new Settings();
		instance.setCollectionFolder("");
		instance.setCollectionFileName("");
		instance.setCollectionType(COLLECTION_FOLDER);
		instance.setProblemIndex(0);
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(RECORDSTORE_NAME, false);
			byte[] data = rs.getRecord(POS_COLLECTION_TYPE);
			instance.collectionType = data[0];
			data = rs.getRecord(POS_FOLDER);
			instance.collectionFolder = new String(data);
			data = rs.getRecord(POS_FILENAME);
			instance.collectionFileName = new String(data);
			data = rs.getRecord(POS_PROBLEM_INDEX);
			instance.problemIndex = Integer.parseInt(new String(data));
		} catch (Exception e) {
			initNewRecordStore();
		} finally {
			close(rs);
		}
		return instance;
	}
	
	public String getCollectionFolder() {
		return collectionFolder;
	}
	public int getProblemIndex() {
		return problemIndex;
	}

	public void setCollectionFolder(String collectionFolder) {
		this.collectionFolder = collectionFolder;
	}

	public void setProblemIndex(int problemIndex) {
		this.problemIndex = problemIndex;
	}

	public byte getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(byte collectionType) {
		this.collectionType = collectionType;
	}

	public String getCollectionFileName() {
		return collectionFileName;
	}

	public void setCollectionFileName(String collectionFileName) {
		this.collectionFileName = collectionFileName;
	}
}

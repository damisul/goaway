package ru.goproject.goaway.light;

import javax.microedition.rms.RecordStore;

public class Settings {
	private final static String RECORDSTORE_NAME = "GoAway.Light.Settings";
	private final static int POS_PROBLEM_INDEX = 1;
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
			byte[] data = Integer.toString(instance.problemIndex).getBytes();
			rs.setRecord(POS_PROBLEM_INDEX, data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.closeRecordStore();
				} catch (Exception e) {					
				}
			}
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
			byte[] data = "0".getBytes();
			rs.addRecord(data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs);
		}
	}
	
	public static Settings readSettings() {
		instance = new Settings();
		instance.setProblemIndex(0);
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(RECORDSTORE_NAME, false);
			byte[] data = rs.getRecord(POS_PROBLEM_INDEX);
			instance.problemIndex = Integer.parseInt(new String(data));
		} catch (Exception e) {
			initNewRecordStore();
		} finally {
			close(rs);
		}
		return instance;
	}
	
	public int getProblemIndex() {
		return problemIndex;
	}

	public void setProblemIndex(int problemIndex) {
		this.problemIndex = problemIndex;
	}
}

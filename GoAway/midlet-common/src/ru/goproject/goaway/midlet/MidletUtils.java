package ru.goproject.goaway.midlet;

import java.io.InputStream;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class MidletUtils {
	private static Display display;
	private static MIDlet midlet;
	public static void init(MIDlet midlet) {
		MidletUtils.midlet = midlet;
		MidletUtils.display = Display.getDisplay(midlet);
	}
	
	public static void show(Displayable displayable) {
		display.setCurrent(displayable);
	}
	
	public static String getAppName() {
		return midlet.getAppProperty("MIDlet-Name") 
			+ " v" 
			+ midlet.getAppProperty("MIDlet-Version");
	}
	
	public static void showError(Exception e) {
		e.printStackTrace();
		showMessage(LocalizedStrings.getResource(CommonStrings.RES_ERROR), e.getMessage());
	}
	
	public static void showMessage(String title, String message) {
		Alert alert = new Alert (title);
		alert.setString(message);
		alert.setTimeout (Alert.FOREVER);
		MidletUtils.show(alert);			
	}
	
	public static void showLocalizedMessage(String titleId, String messageId) {
		showMessage(LocalizedStrings.getResource(titleId), LocalizedStrings.getResource(messageId));
	}
	
	public static byte[] intToByteArray(int value) {
		return new byte[] {
			(byte)(value >>> 24),
			(byte)(value >>> 16),
			(byte)(value >>> 8),
			(byte)value
		};
	}

	public static int byteArrayToInt(byte [] b) {
		return (b[0] << 24) 
			| ((b[1] & 0xFF) << 16) 
			| ((b[2] & 0xFF) << 8)
			| (b[3] & 0xFF);
	}

	public static void closeQuietly(InputStream stream) {
		if (stream == null) {
			return;
		}
		try {
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

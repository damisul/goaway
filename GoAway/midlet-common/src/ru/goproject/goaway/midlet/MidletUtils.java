package ru.goproject.goaway.midlet;

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

	public static boolean isVisible(Displayable d) {
		return display.getCurrent() == d;
	}	
	
	public static String getAppName() {
		return midlet.getAppProperty("MIDlet-Name") 
			+ " v" 
			+ midlet.getAppProperty("MIDlet-Version");
	}
	
	public static void showError(Exception e) {
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
}

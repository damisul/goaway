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

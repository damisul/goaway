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
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class LocalizedStrings {
	private final static char RESOURCE_NAME_DELIMITER = ':';
	private final static char RESOURCE_COMMENT_PREFIX = ';';
	private final static String I18N_FOLDER = "/i18n/";
	private final static String DEFAULT_LOCALE = "en";
	private final static Hashtable resources = new Hashtable();
	private final static LocalizedStrings instance = new LocalizedStrings();
	
	public static String getResource(String resourceId) {
		String res = (String)resources.get(resourceId);
		if (res == null) {
			return resourceId;
		} else {
			return res;
		}
	}
	
	public void loadStrings(String localeCode, String[] baseNames) throws UnsupportedEncodingException {
		resources.clear();
		for (int i = 0; i < baseNames.length; ++i) {
			loadFile(baseNames[i], localeCode);	
		}
		
	}
	
	private void loadFile(String basename, String localeCode) throws UnsupportedEncodingException {
		InputStream stream = getClass().getResourceAsStream(I18N_FOLDER + basename + '.' + localeCode);
		if (stream == null) {
			localeCode = DEFAULT_LOCALE;
		}
		stream = getClass().getResourceAsStream(I18N_FOLDER + basename + '.' + localeCode);
		StringReader reader = new StringReader(stream);
		while (!reader.isEof()) {
			String st = reader.readLine();
			// пропускаем строки комментариев
			if (st.length() > 0 && st.charAt(0) == RESOURCE_COMMENT_PREFIX) {
				continue;
			}
			int p = st.indexOf(RESOURCE_NAME_DELIMITER);
			if (p != -1) {
				resources.put(st.substring(0, p), st.substring(p + 1));	
			}
		}
	}
	
	public static LocalizedStrings getInstance() {
		return instance;
	}
}

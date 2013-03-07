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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Простой класс для построчного считывания файла
 */
public class StringReader {
	private static final String DEFAULT_ENCODING = "UTF-8";
	private Reader reader;
	private boolean eof;
	
	public StringReader(InputStream stream) throws UnsupportedEncodingException {
		this(stream, DEFAULT_ENCODING);
	}

	public StringReader(InputStream stream, String encoding) throws UnsupportedEncodingException {
		reader = new InputStreamReader(stream, encoding);
		eof = false;
	}
	
	public String readLine() {
		StringBuffer buf = new StringBuffer();
		try {
			int charCode = reader.read();
			while (charCode > 0) {
				char ch = (char)charCode;
				if (ch == '\n') {
					break;
				} else if (ch != '\r') {
					buf.append(ch);				
				}
				charCode = reader.read(); 
			}
			if (charCode < 0) {
				eof = true;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
	
	public boolean isEof() {		
		return eof;
	}
}

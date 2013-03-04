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

package ru.goproject.goaway.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import ru.goproject.goaway.sgf.ParseError;

public class SmartReader {
	private final static int BUFFER_SIZE = 512;
	private final static byte STATE_READY = 0;
	private final static byte STATE_ERROR = 1;
	private final static byte STATE_EOF = 2;
	private Reader reader;
	private byte state;
	private int position;
	private int line;
	private int column;
	private char currentChar;
	
	private char[] buffer;
	private int bufferPos;
	private int bufferRealSize;
	
	public SmartReader(InputStream stream, String encoding) {
		try {
			Reader r = new InputStreamReader(stream, encoding);
			setReader(r);
		} catch (UnsupportedEncodingException e) {
			state = STATE_ERROR;
		}
	}
	
	public SmartReader(InputStream stream, String encoding, FilePos pos) {
		line = pos.getLine();
		column = pos.getColumn();
		position = pos.getPosition();
		try {			
			stream.skip(pos.getPosition() - 1);
			Reader r = new InputStreamReader(stream, encoding);
			setReader(r);
		} catch (IOException e) {
			state = STATE_ERROR;
		}
	}
	
	private void setReader(Reader reader) {
		this.reader = reader;
		line = 1;
		column = 0;
		position = 0;
		try {
			buffer = new char[BUFFER_SIZE];
			bufferPos = 0;
			bufferRealSize = 0;
			next();
			state = STATE_READY;
		} catch (IOException e) {
			state = STATE_ERROR;
		}
	}
	
	public boolean isReady() {
		return state == STATE_READY;
	}
	
	public boolean isEof() {
		return state == STATE_EOF;
	}
	
	public char getCurrentChar() {
		if (state == STATE_READY) {
			return currentChar;
		} else if (state == STATE_EOF) {
			throw new ParseError("Unexpected end of file");
		} else {
			throw new ParseError("Reader isn't ready");
		}
	}
	
	public void next() throws IOException {
		if (state != STATE_READY) {
			return;
		}
		
		++position;
		++bufferPos;
		if (bufferPos >= bufferRealSize) {
			bufferRealSize = reader.read(buffer);
			bufferPos = 0;
		}
		
		if (bufferRealSize == -1) {
			state = STATE_EOF;
		} else {
			currentChar = buffer[bufferPos];
			if (currentChar == '\n') {
				++line;
				column = 0;
			} else {
				++column;
			}
		}
	}
	
	public void read(char c) throws IOException {
		char curChar = getCurrentChar();
		if (curChar != c) {
			throw new ParseError("Unexpected character '" + curChar + "' at line " + line + ", pos " + column + ". '" + c + "' expected");
		} else {
			next();
		}
	}
	
	public String readTill(char terminator, char escapeChar) throws IOException {
		StringBuffer result = new StringBuffer();
		boolean escaped = false;
		char c = getCurrentChar();
		while (!(c == terminator && !escaped)) {
			if (c == escapeChar) {
				if (escaped) {
					result.append(c);
					escaped = false;
				} else {
					escaped = true;
				}
			} else {
				escaped = false;
				result.append(c);
			}
			next();
			if (isEof()) {
				throw new ParseError("Unexpected end of file: '" + terminator + "' expected");
			}
			c = getCurrentChar();
		}
		return result.toString();
	}
	
	public String readTill(char terminator) throws IOException {
		StringBuffer result = new StringBuffer();
		char c = getCurrentChar();
		while (c != terminator) {
			result.append(c);
			next();
			if (isEof()) {
				throw new ParseError("Unexpected end of file: '" + terminator + "' expected");
			}
			c = getCurrentChar();
		}
		return result.toString();
		
	}
	
	public void skipBlanks() throws IOException {
		while (isReady() && (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r')) {
			next();
		}
	}
	
	public FilePos getFilePos() {
		return new FilePos(position, line, column); 
	}
	
	public void close() throws IOException {
		reader.close();
	}
}

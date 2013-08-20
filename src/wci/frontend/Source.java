package wci.frontend;

import java.io.BufferedReader;
import java.io.IOException;

public class Source {

	public static final char EOL = '\n';

	public static final char EOF = (char) 0;

	private BufferedReader reader;
	private String line;
	private int lineNumber;
	private int currentPosition;

	public Source(BufferedReader reader) throws IOException {
		this.lineNumber = 0;
		this.currentPosition = -2;
		this.reader = reader;
	}

	public synchronized char peekChar() throws Exception {
		++currentPosition;
		char character = currentChar();
		--currentPosition;
		return character;
	}

	public char nextChar() throws Exception {
		++currentPosition;
		return currentChar();
	}

	public char currentChar() throws Exception {
		if (currentPosition == -2) {
			readLine();
			return nextChar();
		}

		else if (line == null)
			return EOF;

		// The currentPosition == -1 check is for the --currentPosition.
		else if ((currentPosition == -1) || (currentPosition == line.length()))
			return EOL;

		else if (currentPosition > line.length()) {
			readLine();
			return nextChar();
		} else
			return line.charAt(currentPosition);
	}

	private void readLine() throws IOException {
		line = reader.readLine();
		currentPosition = -1;

		if (line != null)
			++lineNumber;
	}

	public void close() throws Exception {

		if (reader != null)
			try {
				reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				throw ex;
			}

	}

	/**
	 * Beginning Getters and Setters for private fields.
	 */

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

}

package wci.frontend;

import java.io.BufferedReader;
import java.io.IOException;

public class Source {

	public static final char EOL = '\n';
	
	public static final char EOF = (char) 0;
	
	priate BufferedReader reader;
	private String line;
	private int lineNumber;
	private int currentPosition;
	
	public Source(BufferedReader reader) throws IOException{
		this.lineNumber = 0;
		this.currentPosition = -2;
		this.reader = reader;
	}
	
	public char currentChar() throws Exception{
		
	}
	
}

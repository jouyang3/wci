package wci.frontend.pascal.tokens;

import wci.frontend.Source;
import wci.frontend.pascal.PascalToken;
import wci.frontend.pascal.PascalTokenType;
import static wci.frontend.pascal.PascalTokenType.*;

public class PascalWordToken extends PascalToken {

	public PascalWordToken(Source source) throws Exception {
		super(source);
	}
	
	protected void extract() throws Exception{
		StringBuilder textBuffer = new StringBuilder();
		char currentChar = currentChar();
		
		/*
		 * Get the word charactes (letter or gdigit). The scanner has already 
		 * determined that the first character is a letter.
		 */
		while (Character.isLetterOrDigit(currentChar)){
			textBuffer.append(currentChar);
			currentChar = nextChar();
		}
		
		
		text = textBuffer.toString();
		// Is it a reserved word or an identifier?
		type = RESERVED_WORDS.contains(text.toLowerCase()) ? 
				PascalTokenType.valueOf(text.toUpperCase()) : IDENTIFIER;
	}
	

}

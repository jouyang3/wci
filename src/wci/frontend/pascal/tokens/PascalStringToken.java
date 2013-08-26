package wci.frontend.pascal.tokens;

import wci.frontend.Source;
import wci.frontend.pascal.PascalToken;

import static wci.frontend.Source.EOF;

import static wci.frontend.pascal.PascalTokenType.STRING;
import static wci.frontend.pascal.PascalTokenType.ERROR;

import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_EOF;

public class PascalStringToken extends PascalToken {

	public PascalStringToken(Source source) throws Exception {
		super(source);
	}
	
	protected void extract() throws Exception{
		StringBuilder textBuffer = new StringBuilder();
		StringBuilder valueBuffer = new StringBuilder();
		
		char currentChar = nextChar();
		textBuffer.append('\'');
		
		do {
			// Replace any whitespace character with a blank
			// Erm... why?
			if( Character.isWhitespace(currentChar))
				currentChar = ' ';
			
			if ((currentChar != '\'') && currentChar != EOF){
				textBuffer.append(currentChar);
				valueBuffer.append(currentChar);
				currentChar = nextChar();
			}
			
			if (currentChar == '\''){
				if(currentChar == '\'' && peekChar() == '\''){
					textBuffer.append("''");
					valueBuffer.append(currentChar);
					currentChar = nextChar();
					currentChar = nextChar(); //to the character beyond the double quotes.
				}
			}
		} while(currentChar != '\'' && currentChar != EOF);
		
		if (currentChar == '\''){
			nextChar(); //consume final quote
			textBuffer.append('\'');
			type = STRING;
			value = valueBuffer.toString();
		} else {
			type = ERROR;
			value = UNEXPECTED_EOF;
		}
		
		text = textBuffer.toString();
	}

}

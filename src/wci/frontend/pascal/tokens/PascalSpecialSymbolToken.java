package wci.frontend.pascal.tokens;

import java.util.Hashtable;
import java.util.Set;

import wci.frontend.Source;
import wci.frontend.pascal.PascalToken;
import static wci.frontend.pascal.PascalTokenType.*;

import static wci.frontend.pascal.PascalErrorCode.INVALID_CHARACTER;

import static wci.util.Utility.*;

public class PascalSpecialSymbolToken extends PascalToken {

	public PascalSpecialSymbolToken(Source source) throws Exception {
		super(source);
	}
	
	private boolean startsWithKey(String keyString, Hashtable<String,?> table){
		Set<String> tableKeys = table.keySet();
		for(String tableKey:tableKeys){
			if(tableKey.startsWith(keyString)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method's efficiency is highly unoptimized!
	 * TODO: Optimize this method.
	 */
	protected void extract() throws Exception{
		Character currentChar = currentChar();
		if(!SPECIAL_SYMBOLS.containsKey(currentChar.toString())){
			nextChar(); //consume bad character
			type = ERROR;
			value = INVALID_CHARACTER;
			return;
		}
		// We will need to keep reading for multiple character symbols
		
		// We write the following if statement for efficiency.
		if(!someEqual(currentChar.toString(), COLON.getText(), GREATER_THAN.getText(),
				LESS_THAN.getText(), DOT.getText())){
			nextChar(); //consume single character special symbol
			type = SPECIAL_SYMBOLS.get(currentChar.toString());
			text = currentChar.toString();
			return;
		}
		
		StringBuilder textBuffer = new StringBuilder();
		textBuffer.append(currentChar);
		//greedily reads the next character, until no further matches occur.
		while(startsWithKey(textBuffer.toString(), SPECIAL_SYMBOLS)){
			String possibleMatch = textBuffer.toString() + peekChar();
			if(!startsWithKey(possibleMatch, SPECIAL_SYMBOLS)){
				nextChar();//consumes the current character
				break;
			}
			/* consumes the next character if token 
			 * still partially matches one of the special symbols.
			 */
			currentChar = nextChar();
			textBuffer.append(currentChar);
		}
		type = SPECIAL_SYMBOLS.get(textBuffer.toString());
		text = textBuffer.toString();
	}

}

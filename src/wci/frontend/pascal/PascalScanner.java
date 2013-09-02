package wci.frontend.pascal;

import static wci.frontend.Source.EOF;
import static wci.frontend.pascal.PascalErrorCode.INVALID_CHARACTER;
import static wci.frontend.pascal.PascalTokenType.END_OF_FILE;
import static wci.util.SexyAssistant.someEqual;
import wci.frontend.EofToken;
import wci.frontend.Scanner;
import wci.frontend.Source;
import wci.frontend.Token;
import wci.frontend.pascal.tokens.PascalErrorToken;
import wci.frontend.pascal.tokens.PascalNumberToken;
import wci.frontend.pascal.tokens.PascalSpecialSymbolToken;
import wci.frontend.pascal.tokens.PascalStringToken;
import wci.frontend.pascal.tokens.PascalWordToken;

public class PascalScanner extends Scanner {

	public PascalScanner(Source source) {
		super(source);
	}

	@Override
	protected Token extractToken() throws Exception {
		skipWhiteSpace();
		Token token;
		char currentChar = currentChar();
		if(currentChar == EOF){
			token = new EofToken(source, END_OF_FILE);
		} else if (Character.isLetter(currentChar)){
			token = new PascalWordToken(source);
		} else if (Character.isDigit(currentChar) || (someEqual(Character.toString(currentChar),
				PascalTokenType.PLUS.getText(), PascalTokenType.MINUS.getText()) && Character.isDigit(peekChar()))){
			token = new PascalNumberToken(source);
		} else if (currentChar == '\''){
			token = new PascalStringToken(source);
		} else if (PascalTokenType.SPECIAL_SYMBOLS.containsKey(Character.toString(currentChar))){
			token = new PascalSpecialSymbolToken(source);
		} else {
			token = new PascalErrorToken(source, INVALID_CHARACTER, Character.toString(currentChar));
			nextChar(); //consumes current character, 
						/*TODO: question: shouldn't we 
								allow consumption until the encounter of first whitepsace? */
		}
		return token;
	}
	
	private void skipWhiteSpace() throws Exception{
		char currentChar = currentChar();
		
		while(Character.isWhitespace(currentChar) || currentChar=='{'){
			if(currentChar == '{'){
				do{currentChar = nextChar(); }while(currentChar != '}' && currentChar != EOF);
				if(currentChar == '}')
					currentChar = nextChar();
			} else {
				currentChar = nextChar();
			}
		}
	}

}

package wci.frontend.pascal;

import static wci.frontend.pascal.PascalErrorCode.IO_ERROR;
import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;
import static wci.frontend.pascal.PascalTokenType.ERROR;
import static wci.message.MessageType.PARSER_SUMMARY;

import java.io.IOException;

import wci.frontend.EofToken;
import wci.frontend.Parser;
import wci.frontend.Scanner;
import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.intermediate.SymTabEntry;
import wci.message.Message;

public class PascalParserTD extends Parser {

	protected static PascalErrorHandler errorHandler;

	static {
		errorHandler = new PascalErrorHandler();
	}

	public PascalParserTD(Scanner scanner) {
		super(scanner);
	}

	@Override
	public void parse() throws Exception {
		Token token;
		long startTime = System.currentTimeMillis();
		try {
			while (!((token = nextToken()) instanceof EofToken)) {
				TokenType tokenType = token.getType();
				
				
//				enables the following lines for debug purpose.
//				if(tokenType != ERROR){
//					sendMessage(new Message(TOKEN, new Object[]{
//						token.getLineNumber(), token.getPosition(),
//						tokenType, token.getText(), token.getValue()
//					}));
//				}
				if(tokenType == IDENTIFIER){
					//Use the text of the token as the keys to the local symtab entry values.
					String name = token.getText().toLowerCase();
					
					/*
					 * If it's not already in the symbol table,
					 * create and enter a new entry for the identifier.
					 */
					SymTabEntry entry = symTabStack.lookup(name);
					if(entry == null){
						entry = symTabStack.enterLocal(name);
					}
					entry.appendLineNumber(token.getLineNumber());
				}
				if(tokenType == ERROR)
					errorHandler.flag(token, (PascalErrorCode) token.getValue(),
						this);
			}
			float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
			
			sendMessage(new Message(PARSER_SUMMARY, new Number[] {
					token.getLineNumber(), getErrorCount(), elapsedTime }));
		} catch (IOException ex) {
			errorHandler.abortTranslation(IO_ERROR, this);
		}

	}

	@Override
	public int getErrorCount() {
		return errorHandler.getErrorCount();
	}

}

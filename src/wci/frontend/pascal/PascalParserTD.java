package wci.frontend.pascal;

import static wci.message.MessageType.PARSER_SUMMARY;
import static wci.message.MessageType.TOKEN;

import java.io.IOException;

import wci.frontend.EofToken;
import wci.frontend.Parser;
import wci.frontend.Scanner;
import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.message.Message;

import static wci.frontend.pascal.PascalErrorCode.IO_ERROR;

import static wci.frontend.pascal.PascalTokenType.ERROR;

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
				
				if(tokenType != ERROR){
					sendMessage(new Message(TOKEN, new Object[]{
						token.getLineNumber(), token.getPosition(),
						tokenType, token.getText(), token.getValue()
					}));
					
				} else
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

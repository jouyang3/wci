package wci.frontend.pascal;

import static wci.frontend.pascal.PascalErrorCode.IO_ERROR;
import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;
import static wci.frontend.pascal.PascalTokenType.BEGIN;
import static wci.message.MessageType.PARSER_SUMMARY;

import java.io.IOException;

import wci.frontend.Parser;
import wci.frontend.Scanner;
import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.frontend.pascal.parsers.StatementParser;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;
import wci.message.Message;

public class PascalParserTD extends Parser {

	protected static PascalErrorHandler errorHandler;

	static {
		errorHandler = new PascalErrorHandler();
	}

	public PascalParserTD(Scanner scanner) {
		super(scanner);
	}
	
	/**
	 * Constructor for subclasses.
	 * @param parent the parent parser.
	 */
	public PascalParserTD(PascalParserTD parent){
		super(parent.getScanner());
	}

	@Override
	public void parse() throws Exception {
		long startTime = System.currentTimeMillis();
		iCode = ICodeFactory.createICode();
		try {
				Token token = nextToken();
				TokenType tokenType = token.getType();
				
				
//				enables the following lines for debug purpose.
//				if(tokenType != ERROR){
//					sendMessage(new Message(TOKEN, new Object[]{
//						token.getLineNumber(), token.getPosition(),
//						tokenType, token.getText(), token.getValue()
//					}));
//				}
//				if(tokenType == IDENTIFIER){
//					//Use the text of the token as the keys to the local symtab entry values.
//					String name = token.getText().toLowerCase();
//					
//					/*
//					 * If it's not already in the symbol table,
//					 * create and enter a new entry for the identifier.
//					 */
//					SymTabEntry entry = symTabStack.lookup(name);
//					if(entry == null){
//						entry = symTabStack.enterLocal(name);
//					}
//					entry.appendLineNumber(token.getLineNumber());
//				}
//				if(tokenType == ERROR)
//					errorHandler.flag(token, (PascalErrorCode) token.getValue(),
//							this);
				ICodeNode rootNode = null;
				if(tokenType == BEGIN){
					StatementParser statementParser = new StatementParser(this);
					rootNode = statementParser.parse(token);
					token = currentToken();
				} else {
					errorHandler.flag(token, UNEXPECTED_TOKEN, this);
				}
				
				token = currentToken();
				
				if(rootNode != null){
					iCode.setRoot(rootNode);
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

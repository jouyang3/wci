package wci.frontend.pascal;

import wci.frontend.Parser;
import wci.frontend.Token;
import wci.message.Message;

import static wci.message.MessageType.SYNTAX_ERROR;

import static wci.frontend.pascal.PascalErrorCode.TOO_MANY_ERRORS;

public class PascalErrorHandler {

	private static final int MAX_ERRORS = 25;

	private static int errorCount = 0;

	/**
	 * Flags an error in the source line.
	 * 
	 * @param token
	 * @param errorCode
	 * @param parser
	 */
	public void flag(Token token, PascalErrorCode errorCode, Parser parser) {
		parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] {
				token.getLineNumber(), token.getPosition(), token.getText(),
				errorCode.toString() }));

		if (++errorCount > MAX_ERRORS) {
			abortTranslation(TOO_MANY_ERRORS, parser);
		}
	}

	/**
	 * Abort the translation process.
	 * 
	 * @param errorCode
	 * @param parser
	 */
	public void abortTranslation(PascalErrorCode errorCode, Parser parser) {
		String fatalText = "FATAL ERROR: " + errorCode.toString();
		parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] { 0, 0, "",
				fatalText }));

		System.exit(errorCode.getStatus());
	}

	public static int getErrorCount() {
		return errorCount;
	}

	public static void setErrorCount(int errorCount) {
		PascalErrorHandler.errorCount = errorCount;
	}

}

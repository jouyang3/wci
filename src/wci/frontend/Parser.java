package wci.frontend;

import wci.intermediate.ICode;
import wci.intermediate.SymTab;
import wci.message.Message;
import wci.message.MessageHandler;
import wci.message.MessageListener;
import wci.message.MessageProducer;
import wci.message.MessageType;

public abstract class Parser implements MessageProducer {

	protected static SymTab symTab;
	protected static MessageHandler messageHandler;
	
	public static final String PARSER_SUMMARY_FORMAT = "\n%,20d source lines.\n%,20d syntax errors.\n%.20.2f seconds total parsing time.\n";

	static {
		symTab = null;
		messageHandler = new MessageHandler();
	}

	protected Scanner scanner;
	protected ICode iCode;

	protected Parser(Scanner scanner) {
		this.scanner = scanner;
		this.iCode = null;
	}

	public abstract void parse() throws Exception;

	public abstract int getErrorCount();

	public Token currentToken() {
		return scanner.currentToken();
	};

	public Token nextToken() throws Exception {
		return scanner.nextToken();
	}

	public void addMessageListener(MessageListener listener) {
		if(listener==null)
			messageHandler.addListener(new ParserMessageListener());
		messageHandler.addListener(listener);
	}

	public void removeMessageListener(MessageListener listener) {
		messageHandler.removeListener(listener);
	}

	public void sendMessage(Message message) {
		messageHandler.sendMessage(message);
	}

	private class ParserMessageListener implements MessageListener {
		public void messageReceived(Message message) {
			MessageType type = message.getType();

			switch (type) {
			case PARSER_SUMMARY: {
				Number body[] = (Number[]) message.getBody();
				int statementCount = (Integer) body[0];
				int syntaxErrors = (Integer) body[1];
				float elapsedTime = (Float) body[2];

				System.out.printf(PARSER_SUMMARY_FORMAT, statementCount,
						syntaxErrors, elapsedTime);
				break;
			}
			}
		}
	}

}

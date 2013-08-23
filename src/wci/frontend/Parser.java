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
		messageHandler.addListener(listener);
	}

	public void removeMessageListener(MessageListener listener) {
		messageHandler.removeListener(listener);
	}

	public void sendMessage(Message message) {
		messageHandler.sendMessage(message);
	}

	public static SymTab getSymTab() {
		return symTab;
	}

	public static void setSymTab(SymTab symTab) {
		Parser.symTab = symTab;
	}

	public static MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public static void setMessageHandler(MessageHandler messageHandler) {
		Parser.messageHandler = messageHandler;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public ICode getiCode() {
		return iCode;
	}

	public void setiCode(ICode iCode) {
		this.iCode = iCode;
	}

}

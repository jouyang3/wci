package wci.frontend;

import wci.message.Message;
import wci.message.MessageHandler;
import wci.message.MessageListener;
import wci.message.MessageProducer;

public abstract class Scanner implements MessageProducer {

	protected Source source;
	private Token currentToken;

	protected abstract Token extractToken() throws Exception;

	public Scanner(Source source) {
		this.source = source;
	}

	public Token currentToken() {
		return currentToken;
	}

	public Token nextToken() throws Exception {
		currentToken = extractToken();
		return currentToken;
	}

	public char currentChar() throws Exception {
		return source.currentChar();
	}

	public char nextChar() throws Exception {
		return source.nextChar();
	}
	
	public char peekChar() throws Exception {
		return source.peekChar();
	}
	
	private static MessageHandler messageHandler;
	
	static{
		messageHandler = new MessageHandler();
	}

	public void addMessageListener(MessageListener listener){
		messageHandler.addListener(listener);
	}

	public void removeMessageListener(MessageListener listener){
		messageHandler.removeListener(listener);
	}

	public void sendMessage(Message message){
		messageHandler.sendMessage(message);
	}

}

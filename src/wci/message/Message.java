package wci.message;

public class Message {

	private MessageType type;
	private Object body;

	public Message(MessageType type, Object body) {
		this.type = type;
		this.body = body;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

}

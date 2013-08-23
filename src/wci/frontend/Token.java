package wci.frontend;

public class Token {

	protected TokenType type;
	protected String text;
	protected Object value;
	protected Source source;
	protected int lineNumber;
	protected int position;

	public Token(Source source) throws Exception {
		this.source = source;
		this.lineNumber = source.getLineNumber();
		this.position = source.getCurrentPosition();

		extract();
	}

	protected void extract() throws Exception {
		text = Character.toString(source.currentChar());
		value = null;
		nextChar();
	}

	protected char currentChar() throws Exception {
		return source.currentChar();
	}

	protected char nextChar() throws Exception {
		return source.nextChar();
	}

	protected char peekChar() throws Exception {
		return source.peekChar();
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}

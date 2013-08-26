package wci.frontend;

public class EofToken extends Token {

	public EofToken (Source source, TokenType type) throws Exception{
		super(source);
		this.type = type;
	}
	
	protected void extract(Source source) throws Exception{
	}
	
}

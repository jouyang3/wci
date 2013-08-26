package wci.frontend.pascal;

import java.util.HashSet;
import java.util.Hashtable;

import wci.frontend.TokenType;

public enum PascalTokenType implements TokenType {

	// Reserved key words.
	AND, ARRAY, BEGIN, CASE, CONST, DIV, DO, DOWNTO, ELSE, END, FILE, FOR,
	FUNCTION, GOTO, IF, IN, LABEL, MOD, NIL, NOT, OF, OR, PACKED, PROCEDURE,
	PROGRAM, RECORD, REPEAT, SET, THEN, TO, TYPE, UNTIL, VAR, WHILE, WITH,
	
	// Special Symbols
	PLUS("+"), MINUS("-"), START("*"), SLASH("/"), COLON_EQUALS(":="), DOT("."),
	COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("'"), EQUALS("="), NOT_EQUALS("<>"),
	LESS_THAN("<"), LESS_EQUALS("<="), GREATER_EQUALS(">="), GREATER_THAN(">"),
	LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACKET("["), RIGHT_BRACKET("]"),
	LEFT_BRACE("{"), RIGHT_BRACE("}"), UP_ARROW("^"), DOT_DOT(".."),
	
	IDENTIFIER, INTEGER, REAL, STRING, ERROR, END_OF_FILE;
	
	public static final int MAX_TOKEN_CHARACTERS = 2;
	
	private static final int FIRST_RESERVED_INDEX = AND.ordinal();
	private static final int LAST_RESERVED_INDEX = WITH.ordinal();
	
	private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
	private static final int LAST_SPECIAL_INDEX = DOT_DOT.ordinal();
	
	private String text;
	
	PascalTokenType(){
		this.text = this.toString().toLowerCase();
	}
	
	PascalTokenType(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
	
	public static HashSet<String> RESERVED_WORDS = new HashSet<String>();
	
	static {
		PascalTokenType values[] = PascalTokenType.values();
		for (int i = FIRST_RESERVED_INDEX; i <= LAST_RESERVED_INDEX; i++){
			RESERVED_WORDS.add(values[i].getText().toLowerCase());
		}
	}
	
	public static Hashtable<String, PascalTokenType> SPECIAL_SYMBOLS = 
			new Hashtable<String, PascalTokenType>();
	
	static{
		PascalTokenType values[] = PascalTokenType.values();
		for (int i = FIRST_SPECIAL_INDEX; i<=LAST_SPECIAL_INDEX; i++){
			SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
		}
	}
}

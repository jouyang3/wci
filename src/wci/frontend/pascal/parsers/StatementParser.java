package wci.frontend.pascal.parsers;

import static wci.frontend.pascal.PascalTokenType.IDENTIFIER;
import static wci.frontend.pascal.PascalTokenType.SEMICOLON;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.LINE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.NO_OP;
import wci.frontend.EofToken;
import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.frontend.pascal.PascalErrorCode;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;

public class StatementParser extends PascalParserTD {

	public StatementParser(PascalParserTD parent) {
		super(parent);
	}

	public ICodeNode parse(Token token) throws Exception {
		ICodeNode statementNode = null;

		switch ((PascalTokenType) token.getType()) {
			case BEGIN: {
				CompoundStatementParser compoundParser = new CompoundStatementParser(this);
				statementNode = compoundParser.parse(token);
				break;
			}
			case IDENTIFIER: {
				AssignmentStatementParser assignmentParser = new AssignmentStatementParser(this);
				statementNode = assignmentParser.parse(token);
				break;
			}
			default: {
				statementNode = ICodeFactory.createICodeNode(NO_OP);
				token = nextToken(); //for hack
				break;
			}
		}
		
		setLineNumber(statementNode, token);
		return statementNode;
	}
	
	protected void setLineNumber(ICodeNode node, Token token){
		if(node!=null)
			node.setAttribute(LINE, token.getLineNumber());
	}
	
	/**
	 * Parse a statement list (That of compounds or assignment).
	 * @param token the current token.
	 * @param parentNode the parent node of the statement list.
	 * @param terminator the token type of the node that terminates the list.
	 * @param errorCode the error code if the terminator token is missing.
	 * @throws Exception if an error occurred.
	 */
	protected void parseList(Token token, ICodeNode parentNode,
							PascalTokenType terminator,
							PascalErrorCode errorCode)
	throws Exception{
		/*
		 * Loop to parse each statement until the END token or the end
		 * of the source file.
		 */
		while(!(token instanceof EofToken) && token.getType() != terminator){
			
			// Parse a statement. The parent node adopts the statement node.
			ICodeNode statementNode = parse(token);
			parentNode.addChild(statementNode);
			
			token = currentToken();
			TokenType tokenType = token.getType();
			
			// Look for the semicolon between statements.
			if (tokenType == SEMICOLON){
				token = nextToken();
			}
			
			else if (tokenType == IDENTIFIER){
				errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
				token = nextToken();
			}
			
		}
		
		if (token.getType() == terminator){
			token = nextToken();
		} else {
			errorHandler.flag(token, errorCode, this);
		}
	}

}

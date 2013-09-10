package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.pascal.PascalErrorCode;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;
import wci.intermediate.icodeimpl.ICodeKeyImpl;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

import wci.frontend.pascal.parsers.ExpressionParser;

public class AssignmentStatementParser extends StatementParser {

	public AssignmentStatementParser(PascalParserTD parent) {
		super(parent);
	}
	
	public ICodeNode parse(Token token) throws Exception{
		ICodeNode assignNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.ASSIGN);
		String targetName = token.getText().toLowerCase();
		SymTabEntry symTabEntry = symTabStack.lookupLocal(targetName);
		if(symTabEntry == null)
			symTabEntry = symTabStack.enterLocal(targetName);
		
		symTabEntry.appendLineNumber(token.getLineNumber());
		
		token = nextToken();
		
		ICodeNode variableNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.VARIABLE);
		variableNode.setAttribute(ICodeKeyImpl.ID, symTabEntry);
		
		assignNode.addChild(variableNode);
		
		if(token.getType() == PascalTokenType.COLON_EQUALS){
			token = nextToken();
		} else {
			errorHandler.flag(token, PascalErrorCode.MISSING_COLON_EQUALS, this);
		}
		
		ExpressionParser expressionParser = new ExpressionParser(this);
		assignNode.addChild(expressionParser.parse(token));
		return assignNode;
	}

}

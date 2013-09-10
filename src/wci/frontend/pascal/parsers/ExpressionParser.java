package wci.frontend.pascal.parsers;

import static wci.frontend.pascal.PascalTokenType.DIV;
import static wci.frontend.pascal.PascalTokenType.EQUALS;
import static wci.frontend.pascal.PascalTokenType.GREATER_EQUALS;
import static wci.frontend.pascal.PascalTokenType.GREATER_THAN;
import static wci.frontend.pascal.PascalTokenType.LESS_EQUALS;
import static wci.frontend.pascal.PascalTokenType.LESS_THAN;
import static wci.frontend.pascal.PascalTokenType.MINUS;
import static wci.frontend.pascal.PascalTokenType.NOT_EQUALS;
import static wci.frontend.pascal.PascalTokenType.OR;
import static wci.frontend.pascal.PascalTokenType.PLUS;
import static wci.frontend.pascal.PascalTokenType.RIGHT_PAREN;
import static wci.frontend.pascal.PascalTokenType.SLASH;
import static wci.frontend.pascal.PascalTokenType.STAR;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.ID;
import static wci.intermediate.icodeimpl.ICodeKeyImpl.VALUE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.ADD;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.EQ;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.FLOAT_DIVIDE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.GE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.GT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.INTEGER_CONSTANT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.INTEGER_DIVIDE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.LT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.MULTIPLY;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.NE;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.NOT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.REAL_CONSTANT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.STRING_CONSTANT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.SUBTRACT;
import static wci.intermediate.icodeimpl.ICodeNodeTypeImpl.VARIABLE;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import wci.frontend.Token;
import wci.frontend.TokenType;
import wci.frontend.pascal.PascalErrorCode;
import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalTokenType;
import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeNodeType;
import wci.intermediate.SymTabEntry;
import wci.intermediate.icodeimpl.ICodeNodeTypeImpl;

/**
 * Expression > Simple Expression > Term > Factor
 * 
 * For syntax diagram, see wci page 122.
 * 
 * @author jun
 * 
 */
public class ExpressionParser extends StatementParser {

	private static final EnumSet<PascalTokenType> REL_OPS = EnumSet.of(EQUALS,
			NOT_EQUALS, LESS_THAN, LESS_EQUALS, GREATER_THAN, GREATER_EQUALS);

	private static final Map<PascalTokenType, ICodeNodeType> REL_OPS_MAP = new HashMap<PascalTokenType, ICodeNodeType>();

	static {
		REL_OPS_MAP.put(EQUALS, EQ);
		REL_OPS_MAP.put(NOT_EQUALS, NE);
		REL_OPS_MAP.put(LESS_THAN, LT);
		REL_OPS_MAP.put(LESS_EQUALS, LE);
		REL_OPS_MAP.put(GREATER_THAN, GT);
		REL_OPS_MAP.put(GREATER_EQUALS, GE);
	}

	public ExpressionParser(PascalParserTD parent) {
		super(parent);
	}

	public ICodeNode parse(Token token) throws Exception {
		return parseExpression(token);
	}

	/**
	 * Parse an expression.
	 * 
	 * @param token
	 *            the initial token.
	 * @return the root of the generated parse subtree.
	 * @throws Exception
	 *             if an error occurred.
	 */
	private ICodeNode parseExpression(Token token) throws Exception {

		// Process LHS
		ICodeNode rootNode = parseSimpleExpression(token);

		// Now we are either at the end of the expression or at an relational
		// operator.
		token = currentToken();
		TokenType tokenType = token.getType();

		if (REL_OPS.contains(tokenType)) {

			ICodeNodeType nodeType = REL_OPS_MAP.get(tokenType);
			ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
			opNode.addChild(rootNode);

			token = nextToken(); // consumes the operator.

			// Process RHS
			opNode.addChild(parseSimpleExpression(token));

			rootNode = opNode;
		}
		return rootNode;
	}

	private static final EnumSet<PascalTokenType> ADD_OPS = EnumSet.of(PLUS,
			MINUS, OR);

	private static final Map<PascalTokenType, ICodeNodeTypeImpl> ADD_OPS_OPS_MAP = new HashMap<PascalTokenType, ICodeNodeTypeImpl>();

	static {
		ADD_OPS_OPS_MAP.put(PLUS, ADD);
		ADD_OPS_OPS_MAP.put(MINUS, SUBTRACT);
		ADD_OPS_OPS_MAP.put(PascalTokenType.OR, ICodeNodeTypeImpl.OR);
	}

	// this might not work as +/- is not a term.
	private ICodeNode parseSimpleExpression(Token token) throws Exception {
		TokenType signType = null;
		TokenType tokenType = token.getType();
		
		if((tokenType == PLUS) || (tokenType == MINUS)){
			signType = tokenType;
			token = nextToken();
		}
		
		ICodeNode rootNode = parseTerm(token);
		
		if(signType == MINUS){ //TODO: re-allow negation numbers
			ICodeNode negateNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NEGATE);
			negateNode.addChild(rootNode);
			rootNode = negateNode;
		}
		
		token = currentToken();
		tokenType = token.getType();
		
		while(ADD_OPS.contains(tokenType)){
			ICodeNodeType nodeType = ADD_OPS_OPS_MAP.get(tokenType);
			ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
			opNode.addChild(rootNode);
			token = nextToken();
			opNode.addChild(parseTerm(token));
			rootNode = opNode;
			token = currentToken();
			tokenType = token.getType();
		}

		return rootNode;
	}

	private static final EnumSet<PascalTokenType> MULT_OPS = EnumSet.of(STAR,
			SLASH, DIV, PascalTokenType.MOD, PascalTokenType.AND);

	private static final Map<PascalTokenType, ICodeNodeType> MULT_OPS_OPS_MAP = new HashMap<PascalTokenType, ICodeNodeType>();

	static {
		MULT_OPS_OPS_MAP.put(STAR, MULTIPLY);
		MULT_OPS_OPS_MAP.put(SLASH, FLOAT_DIVIDE);
		MULT_OPS_OPS_MAP.put(DIV, INTEGER_DIVIDE);
		MULT_OPS_OPS_MAP.put(PascalTokenType.MOD, ICodeNodeTypeImpl.MOD);
		MULT_OPS_OPS_MAP.put(PascalTokenType.AND, ICodeNodeTypeImpl.AND);
	}

	private ICodeNode parseTerm(Token token) throws Exception {
		ICodeNode rootNode = parseFactor(token);
		token = currentToken();
		PascalTokenType tokenType = (PascalTokenType) token.getType();
		while (MULT_OPS.contains(tokenType)) {
			ICodeNodeType nodeType = MULT_OPS_OPS_MAP.get(tokenType);
			ICodeNode opNode = ICodeFactory.createICodeNode(nodeType);
			opNode.addChild(rootNode);
			token = nextToken();
			opNode.addChild(parseFactor(token));
			token = currentToken();
			rootNode = opNode;
			tokenType = (PascalTokenType) token.getType();
		}
		return rootNode;
	}

	private ICodeNode parseFactor(Token token) throws Exception {
		TokenType tokenType = token.getType();
		ICodeNode rootNode = null;
		switch ((PascalTokenType) tokenType) {
		case IDENTIFIER: {
			/*
			 * Look up the identifier in the local symbol table. Report error if
			 * identifier is undefined.
			 */
			String name = token.getText().toLowerCase();
			SymTabEntry id = symTabStack.lookupLocal(name);
			if (id == null) {
				errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED,
						this);
				id = symTabStack.enterLocal(name); // TODO: REMOVE THIS HACK IN
													// THE FUTURE! WE ARE NOT
													// TYPE-LOOSE SCRIPTING!
			}

			rootNode = ICodeFactory.createICodeNode(VARIABLE);
			rootNode.setAttribute(ID, id);
			id.appendLineNumber(token.getLineNumber());

			token = nextToken();
			break;
		}
		case REAL: {
			rootNode = ICodeFactory.createICodeNode(REAL_CONSTANT);
			rootNode.setAttribute(VALUE, token.getValue());
			token = nextToken();
			break;
		}
		case INTEGER: {
			rootNode = ICodeFactory.createICodeNode(INTEGER_CONSTANT);
			rootNode.setAttribute(VALUE, token.getValue());
			token = nextToken();
			break;
		}
		case STRING: {
			rootNode = ICodeFactory.createICodeNode(STRING_CONSTANT);
			rootNode.setAttribute(VALUE, token.getValue());
			token = nextToken();
			break;
		}
		case NOT: {
			rootNode = ICodeFactory.createICodeNode(NOT);
			rootNode.addChild(parseFactor(token = nextToken()));
			break;
		}
		case LEFT_PAREN: {
			token = nextToken(); // consumes the (
			rootNode = parseExpression(token);
			token = currentToken();
			if (token.getType() == RIGHT_PAREN) {
				token = nextToken(); // consumes the )
			} else {
				errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_PAREN,
						this);
			}
			break;
		}
		default: {
			errorHandler.flag(token, PascalErrorCode.UNEXPECTED_TOKEN, this);
		}
		}
		return rootNode;
	}

}

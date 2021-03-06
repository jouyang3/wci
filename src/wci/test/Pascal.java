package wci.test;

import static wci.frontend.pascal.PascalTokenType.STRING;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import wci.backend.Backend;
import wci.backend.BackendFactory;
import wci.frontend.FrontendFactory;
import wci.frontend.Parser;
import wci.frontend.Source;
import wci.frontend.TokenType;
import wci.intermediate.ICode;
import wci.intermediate.SymTabStack;
import wci.message.Message;
import wci.message.MessageListener;
import wci.message.MessageType;
import wci.util.CrossReferencer;
import wci.util.ParseTreePrinter;

public class Pascal {

	private Parser parser;
	private Source source;
	private ICode iCode;
	private SymTabStack symTabStack;
	private Backend backend;
	public static String README_MD_FILE = "README.MD";
	public static PrintStream ps;
			
	static{
		try{
			ps = new PrintStream(README_MD_FILE);
		} catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Pascal(String operation, String filePath, String flags) {
		try {
			boolean intermediate = flags.indexOf('i') > -1;
			boolean xref = flags.indexOf('x') > -1;

			source = new Source(new BufferedReader(new FileReader(filePath)));
			source.addMessageListener(new SourceMessageListener());

			parser = FrontendFactory.createParser("Pascal", "top-down", source);
			parser.addMessageListener(new ParserMessageListener());

			backend = BackendFactory.createBackend(operation);
			backend.addMessageListener(new BackendMessageListener());

			parser.parse();
			
//			source.close();

			iCode = parser.getiCode();
			symTabStack = parser.getSymTabStack();
			
			if(xref){
				CrossReferencer crossReferencer = new CrossReferencer();
				crossReferencer.print(symTabStack);
			}
			if(intermediate){
				ParseTreePrinter treePrinter = new ParseTreePrinter(ps);
				treePrinter.printPrettyXML(iCode);
			}

			backend.process(iCode, symTabStack);
		} catch (Exception ex) {
			ps.println("***** Internal translator+JVM error. *****");
			ex.printStackTrace();
		}
	}
	
	public static void main(String...args){
		try{
			String operation = args[0];
			
			int i = 0;
			String flags = "";
			
			while((++i < args.length) && args[i].charAt(0) == '-'){
				flags += args[i].substring(1);
			}
			
			if(i<args.length){
				String path = args[i];
				new Pascal(operation, path, flags);
			} else
				throw new Exception();
			
		} catch (Exception ex){
			ps.println(USAGE);
		}
	}

	private static final String FLAGS = "[-ix]";
	private static final String USAGE = "Usage: Pascal execute|compile "
			+ FLAGS + " <source file path>";

	private static final String SOURCE_LINE_FORMAT = "%03d %s";

	private class SourceMessageListener implements MessageListener {
		
		@Override
		public void messageReceived(Message message) {
			MessageType type = message.getType();
			Object body[] = (Object[]) message.getBody();

			switch (type) {
			case SOURCE_LINE: {
				int lineNumber = (Integer) body[0];
				String lineText = (String) body[1];

				ps.println(String.format(SOURCE_LINE_FORMAT,
						lineNumber, lineText));

				break;
			}

			}
		}

	}
	
	private static final String PARSER_SUMMARY_FORMAT = 
			"\n%,20d source lines.\n%,20d syntax errors.\n%,20.2f seconds total parsing time.\n";
	private static final String TOKEN_FORMAT = ">>> %-15s line=%03d, pos=%2d, text=\"%s\"";
	private static final String VALUE_FORMAT = ">>>          value=%s";
	
	private static final int PREFIX_WIDTH = 5;
	
	private class ParserMessageListener implements MessageListener{

		@Override
		public void messageReceived(Message message) {
			MessageType type = message.getType();
			
			switch(type){
			case PARSER_SUMMARY: {
				Number body[] = (Number[]) message.getBody();
				int statementCount = (Integer) body[0];
				int syntaxErrors = (Integer) body[1];
				float elapsedTime = (Float) body[2];
				ps.print("\n===== BEGIN FRONTEND PARSER =====\n");
				ps.printf(PARSER_SUMMARY_FORMAT, statementCount, syntaxErrors, elapsedTime);
				ps.print("\n===== END FRONTEND PARSER =====");
				break;
			}
			case TOKEN: {
				Object body[] = (Object[]) message.getBody();
				int line = (Integer) body[0];
				int position = (Integer) body[1];
				TokenType tokenType = (TokenType) body[2];
				String tokenText = (String) body[3];
				Object tokenValue = body[4];
				
				ps.println(String.format(TOKEN_FORMAT, tokenType,
						line, position, tokenText));
				
				if(tokenValue!=null){
					if(tokenType == STRING)
						tokenValue = "\""+tokenValue+"\"";
					
					ps.println(String.format(VALUE_FORMAT, tokenValue));
				}
				break;
			}
			case SYNTAX_ERROR: {
				Object body[] = (Object[]) message.getBody();
				int lineNumber = (Integer) body[0];
				int position = (Integer) body[1];
				String tokenText = (String) body[2];
				String errorMessage = (String) body[3];
				
				// Calculates the cursor position
				int spaceCount = PREFIX_WIDTH+position;
				
				StringBuilder flagBuffer = new StringBuilder();
				
				/*
				 * pad to before the error position, the one extra space 
				 * is saved for the cursor.
				 */
				for(int i=1; i<spaceCount; ++i){
					flagBuffer.append(' ');
				}
				
				flagBuffer.append("^\n*** ").append(errorMessage);
				
				if(tokenText != null)
					flagBuffer.append(" [at \"").append(tokenText).append("\"]");
				
				ps.println(flagBuffer.toString());
				break;
				
			}
			}
		}
	}
	
	private static final String INTERPRETER_SUMMARY_FORMAT =
			"\n%,20d statements executed.\n%,20d runtime errors.\n%,20.2f seconds total execution time.\n";
	
	private static final String COMPILER_SUMMARY_FORMAT =
			"\n%,20d instructions generated.\n%,20.2f seconds total code generation time.\n";
	
	private static final String BACKEND_TITLE = " BACKEND ";
	private static final String BACKEND_BAR = "=====";
	
	private class BackendMessageListener implements MessageListener{

		@Override
		public void messageReceived(Message message) {
			
			ps.print("\n"+BACKEND_BAR+" BEGIN"+BACKEND_TITLE+BACKEND_BAR+"\n");
			
			MessageType type = message.getType();
			switch(type){
			case INTERPRETER_SUMMARY: {
				Number body[] = (Number[]) message.getBody();
				int executionCount = (Integer) body[0];
				int runtimeErrors = (Integer) body[1];
				float elapsedTime = (Float) body[2];
				
				
				
				ps.printf(INTERPRETER_SUMMARY_FORMAT, executionCount, runtimeErrors, elapsedTime);
				break;
			}
			case COMPILER_SUMMARY: {
				Number body[] = (Number[]) message.getBody();
				int instructionCount = (Integer) body[0];
				float elapsedTime = (Float) body[1];
				
				ps.printf(COMPILER_SUMMARY_FORMAT, instructionCount, elapsedTime);
				break;
			}
			}
			ps.print("\n"+BACKEND_BAR+" END"+BACKEND_TITLE+BACKEND_BAR);
		}
		
	}

}

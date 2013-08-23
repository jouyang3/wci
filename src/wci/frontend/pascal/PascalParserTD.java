package wci.frontend.pascal;

import wci.frontend.EofToken;
import wci.frontend.Parser;
import wci.frontend.Scanner;
import wci.frontend.Token;
import wci.message.Message;

import static wci.message.MessageType.PARSER_SUMMARY;

public class PascalParserTD extends Parser {
	
	

	public PascalParserTD(Scanner scanner) {
		super(scanner);
	}
	
	@Override
	public void parse() throws Exception {
		Token token;
		long startTime = System.currentTimeMillis();
		while(!((token = nextToken()) instanceof EofToken)) {}
		
		float elapsedTime = (System.currentTimeMillis() - startTime) /1000f;
		
		sendMessage(new Message(PARSER_SUMMARY, 
				new Number[]{token.getLineNumber(),
							getErrorCount(),
							elapsedTime
		}));
	}

	@Override
	public int getErrorCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

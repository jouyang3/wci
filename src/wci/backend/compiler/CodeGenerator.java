package wci.backend.compiler;

import wci.backend.Backend;
import wci.intermediate.ICode;
import wci.intermediate.SymTab;
import wci.message.Message;
import wci.message.MessageListener;

import static wci.message.MessageType.COMPILER_SUMMARY;

public class CodeGenerator extends Backend {

	@Override
	public void process(ICode iCode, SymTab symTab) throws Exception {
		long startTime = System.currentTimeMillis();
		float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
		int instructionCount = 0;
		
		// Send the compiler summary message.
		sendMessage(new Message(COMPILER_SUMMARY,
				new Number[]{instructionCount, elapsedTime}));
	}

}

package wci.backend.compiler;

import static wci.message.MessageType.COMPILER_SUMMARY;
import wci.backend.Backend;
import wci.intermediate.ICode;
import wci.intermediate.SymTabStack;
import wci.message.Message;

public class CodeGenerator extends Backend {

	@Override
	public void process(ICode iCode, SymTabStack symTabStack) throws Exception {
		long startTime = System.currentTimeMillis();
		float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
		int instructionCount = 0;
		
		// Send the compiler summary message.
		sendMessage(new Message(COMPILER_SUMMARY,
				new Number[]{instructionCount, elapsedTime}));
	}

}

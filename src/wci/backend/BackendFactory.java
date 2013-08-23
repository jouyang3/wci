package wci.backend;

import wci.backend.compiler.CodeGenerator;
import wci.backend.interpreter.Executor;

public class BackendFactory {
	
	/**
	 * 
	 * @param operation "compile" | "execute"
	 * @return
	 * @throws Exception
	 */
	public static Backend createBackend(String operation) throws Exception{
		if(operation.equalsIgnoreCase("compile"))
			return new CodeGenerator();
		else if(operation.equalsIgnoreCase("execute"))
			return new Executor();
		else
			throw new Exception("Backend Factory: Invalid operation '"+operation+"'");
			
	}

}

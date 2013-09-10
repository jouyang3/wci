package wci.intermediate;

import wci.intermediate.icodeimpl.ICodeImpl;
import wci.intermediate.icodeimpl.ICodeNodeImpl;

public class ICodeFactory {

	public static ICode createICode(){
		return new ICodeImpl(); 
	}
	
	public static ICodeNode createICodeNode(ICodeNodeType type){
		return new ICodeNodeImpl(type);
	}
	
	public static ICodeNode createICodeNode(ICodeNodeType type, ICodeNode parentNode){
		return new ICodeNodeImpl(type, parentNode);
	}
	
}

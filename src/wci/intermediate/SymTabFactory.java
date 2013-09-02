package wci.intermediate;

import wci.intermediate.symtabimpl.SymTabEntryImpl;
import wci.intermediate.symtabimpl.SymTabImpl;
import wci.intermediate.symtabimpl.SymTabStackImpl;

public class SymTabFactory {

	public static SymTabStack createSymTabStack(){
		return new SymTabStackImpl();
	}
	
	public static SymTab createSymTab(int nestingLevel){
		return new SymTabImpl(nestingLevel);
	}
	
	public static SymTabEntry createSymTabEntry(String name, SymTab symTab){
		return new SymTabEntryImpl(name, symTab);
	}
	
}

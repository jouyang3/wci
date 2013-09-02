package wci.intermediate;

public interface SymbolTabStack {

	public int getCurrentNestingLevel();
	
	public SymTab getLocalSymTab();
	
	public SymTabEntry enterLocal();
	
	public SymTabEntry lookupLocal();
	
	public SymTabEntry lookup();
	
}

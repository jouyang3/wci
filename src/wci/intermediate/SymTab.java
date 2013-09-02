package wci.intermediate;

import java.util.List;

public interface SymTab {

	public int getNestingLevel();
	
	public SymTabEntry enter();
	
	public SymTabEntry lookup();
	
	public List<SymTabEntry> sortedEntries();
}

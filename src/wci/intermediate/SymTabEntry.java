package wci.intermediate;

import java.util.List;

public interface SymTabEntry {
	
	public String getName();
	
	public SymTab getSymTab();
	
	public void appendLineNumber();
	
	public List<Number> getLineNumbers();
	
	public void setAttribute();
	
	public Object getAttribute();
	
}

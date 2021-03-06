package wci.intermediate;

import java.util.List;

/**
 * <h1>SymTab</h1>
 * <p>The framework interface that represents the symbol table.</p>
 * @author jun
 *
 */
public interface SymTab {

	/**
	 * Getter.
	 * @return the scope nesting level of this entry.
	 */
	public int getNestingLevel();
	
	/**
	 * Create and enter a new entry into the symbol table.
	 * @param name the name of the entry.
	 * @return the new entry.
	 */
	public SymTabEntry enter(String name);
	
	/**
	 * Look up an existing symbol table entry.
	 * @param name the name of the entry.
	 * @return the entry, or null if it does not exist.
	 */
	public SymTabEntry lookup(String name);
	
	/**
	 * @return a list of symbol table entries sorted by name.
	 */
	public List<SymTabEntry> sortedEntries();
}

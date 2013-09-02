package wci.intermediate.symtabimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import wci.intermediate.SymTab;
import wci.intermediate.SymTabEntry;
import wci.intermediate.SymTabFactory;

public class SymTabImpl extends TreeMap<String, SymTabEntry> implements SymTab{
	
	private int nestingLevel;
	
	public SymTabImpl(int nestingLevel){
		this.nestingLevel = nestingLevel;
	}
	
	@Override
	public int getNestingLevel() {
		return nestingLevel;
	}

	@Override
	public SymTabEntry enter(String name) {
		SymTabEntry entry = SymTabFactory.createSymTabEntry(name, this);
		put(name, entry);
		return entry;
	}

	@Override
	public SymTabEntry lookup(String name) {
		return get(name);
	}

	@Override
	public List<SymTabEntry> sortedEntries() {
		List<SymTabEntry> list = new ArrayList<SymTabEntry>(size());
		list.addAll(values());
		return list;
	}

}

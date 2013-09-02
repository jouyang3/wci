package wci.intermediate.symtabimpl;

import java.util.ArrayList;

import wci.intermediate.SymTab;
import wci.intermediate.SymTabEntry;
import wci.intermediate.SymTabFactory;
import wci.intermediate.SymTabStack;

public class SymTabStackImpl extends ArrayList<SymTab> implements SymTabStack{
	
	private int currentNestingLevel;
	
	/**
	 * Creates a SymTabStack and adds an initial local SymTab on top.
	 */
	public SymTabStackImpl(){
		this.currentNestingLevel = 0;
		add(SymTabFactory.createSymTab(currentNestingLevel));
	}

	@Override
	public int getCurrentNestingLevel() {
		return currentNestingLevel;
	}

	@Override
	public SymTab getLocalSymTab() {
		return get(currentNestingLevel);
	}

	@Override
	public SymTabEntry enterLocal(String name) {
		return get(currentNestingLevel).enter(name);
	}

	@Override
	public SymTabEntry lookupLocal(String name) {
		return get(currentNestingLevel).lookup(name);
	}

	@Override
	public SymTabEntry lookup(String name) {
		return lookupLocal(name);
	}

}

package wci.util;

import java.util.List;

import wci.intermediate.SymTab;
import wci.intermediate.SymTabEntry;
import wci.intermediate.SymTabStack;

/**
 * <h1>CrossReferencer</h1>
 * <p>Generate a cross-reference listing.</p>
 * @author jun
 */
public class CrossReferencer {
	

	private static final int NAME_WIDTH = 25;
	
	private static final String TABLE_TITLE = "==== CROSS-REFERENCE TABLE ====";
	private static final String NAME_FORMAT = "%-" + NAME_WIDTH + "s";
	private static final String IDENTIFIER_LABEL = "Identifier";
	private static final String NUMBERS_LABEL = "Line Numbers";
	private static final String UNDERLINE = "------------";
	private static final String NUMBER_FORMAT = "%03d";
	private static final String COLUMN_HEADING_FORMAT = NAME_FORMAT+" %s"
													+"\n"+NAME_FORMAT+" %s";
	
	private static final int LABEL_WIDTH = NUMBERS_LABEL.length();
	private static final int INDENT_WIDTH = NAME_WIDTH + LABEL_WIDTH;
	
	private static final StringBuilder INDENT = new StringBuilder(INDENT_WIDTH);
	static{
		for(int i=0; i<INDENT_WIDTH; ++i) INDENT.append(" ");
	}
	
	public void print(SymTabStack symTabStack){
		System.out.println("\n"+TABLE_TITLE);
		printColumnHeadings();
		
		printSymTab(symTabStack.getLocalSymTab());
	}
	
	public void printColumnHeadings(){
		System.out.println();
		
		System.out.println(String.format(
				COLUMN_HEADING_FORMAT, IDENTIFIER_LABEL, NUMBERS_LABEL, UNDERLINE, UNDERLINE));
	}
	
	private void printSymTab(SymTab symTab){
		List<SymTabEntry> sorted = symTab.sortedEntries();
		for(SymTabEntry entry:sorted){
			List<Integer> lineNumbers = entry.getLineNumbers();
			
			System.out.print(String.format(NAME_FORMAT, entry.getName()));
			if(lineNumbers != null){
				for (Integer lineNumber : lineNumbers){
					System.out.print(" "+String.format(NUMBER_FORMAT, lineNumber));
				}
			}
			System.out.println();
		}
	}
	
	
	
}

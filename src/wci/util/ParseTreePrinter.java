package wci.util;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wci.intermediate.ICode;
import wci.intermediate.ICodeKey;
import wci.intermediate.ICodeNode;
import wci.intermediate.SymTabEntry;
import wci.intermediate.icodeimpl.ICodeNodeImpl;

public class ParseTreePrinter {

	public static final String OUTPUT_TITLE = "INTERMEDIATE CODE";
	private static final String OUTPUT_HEAD = "\n===== " + OUTPUT_TITLE
			+ " =====\n";

	private static final int INDENT_WIDTH = 4;
	private static int LINE_WIDTH = 80;

	private PrintStream ps;
	private int length;
	private String indent;
	//indentation = indent * indentLevel must never exceed the LINE_WIDTH value.
	//Otherwise, stackoverflow error will occur.
	//TODO: CHECK the reason for this error.
	private int indentLevel;
	private StringBuilder line;

	public ParseTreePrinter(PrintStream ps) {
		this.ps = ps;
		this.length = 0;
		this.indentLevel = 1;
		this.line = new StringBuilder();

		this.indent = "";
		for (int i = 0; i < INDENT_WIDTH; ++i)
			this.indent += " ";
	}

	public void printPrettyXML(ICode iCode) {
		ps.println(OUTPUT_HEAD);
		// With the constant casting, how is this generic?
		printNode((ICodeNodeImpl) iCode.getRoot());
		printLine();
		ps.flush(); // in the case of a buffered writer.
	}

	private void printNode(ICodeNodeImpl node) {
		append("\n"+indent(indentLevel));
		append("<" + node.toString());
		printAttributes(node);
		printTypeSpec(node);

		List<ICodeNode> childNodes = node.getChildren();
		if (childNodes != null && childNodes.size() > 0) {
			append(">");
			indentLevel++;
			for (ICodeNode childNode : childNodes) {
				printNode((ICodeNodeImpl) childNode);
			}
			indentLevel--;
			append("\n"+indent(indentLevel));
			append("</" + node.toString() + ">");
		} else {
			append(" ");
			append("/>");
		}
		//printLine();
	}

	private void printAttributes(ICodeNodeImpl node) {
		Iterator<Map.Entry<ICodeKey, Object>> they = node.entrySet().iterator();
		while (they.hasNext()) {
			Map.Entry<ICodeKey, Object> attribute = they.next();
			printAttribute(attribute.getKey().toString(), attribute.getValue());
		}
	}

	private void printAttribute(String keyString, Object value) {
		// If the value is a symbol table entry (identifier), use the
		// identifier's name;
		// otherwise, just use the value ('value' or otherwise defined) string.
		boolean isSymTabEntry = value instanceof SymTabEntry;
		String valueString = isSymTabEntry ? ((SymTabEntry) value).getName()
				: value.toString();
		String text = keyString.toLowerCase() + "=\"" + valueString + "\"";
		append(" ");
		append(text);

		if (isSymTabEntry) { // function entry
			int level = ((SymTabEntry) value).getSymTab().getNestingLevel();
			printAttribute("LEVEL", level);
		}
	}

	private void printTypeSpec(ICodeNodeImpl node) {

	}

	/**
	 * What if length or textLength itself exceeds LINE_WIDTH? This method is
	 * created in the light of that situation.
	 * 
	 * @param text
	 *            The text to be printed.
	 */
	public void append(String text) {
		line.append(text);
		wrapLine();
	}
	
	private void wrapLine(){
		String remain = chopText(line.toString());
		line.setLength(0);
		line.append(remain);
		length = line.length();
	}
	
	private String chopText(String text){
		int textLength = text.length();
		if(text.contains("\n")){
			int nlpos = text.indexOf('\n');
			String beforeNl = text.substring(0,nlpos);
			String afterNl = text.substring(nlpos+1);
			if(nlpos>LINE_WIDTH){ /* if newline position is greater 
									* than the allowed display 
									* region, we chop the text
									* since new line is not space
									* consuming.
									*/
				text = chopText(beforeNl)+afterNl;
			} else {//otherwise, we display right-away
				ps.println(beforeNl);
				text = afterNl;
			}
			textLength = text.length();
		}
		if(textLength > LINE_WIDTH){
			ps.println(text.substring(0, LINE_WIDTH));
			//cannot use the indentation as it is updated!
			//we must allow the use of the indentation before updated!
			//But passing indentation all the time to append is very
			//tedious, so we must revise our algorithm.
			return chopText(indent(indentLevel)+text.substring(LINE_WIDTH, textLength));
		} else {
			return text;
		}
	}
	
	private String indent(int level){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<level;i++){
			sb.append(indent);
		}
		return sb.toString();
	}

	private void printLine() {
		if (length > 0) { // safe-guard to prevent extra printing.
			ps.println(line.toString());
			line.setLength(0);
			length = 0;
		}
	}
	
	public StringBuilder getLine() {
		return line;
	}

	public void setLine(StringBuilder line) {
		this.line = line;
	}

	public static void setMaxLineWidth(int width) {
		LINE_WIDTH = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getIndentLevel() {
		return indentLevel;
	}

	public void setIndentLevel(int indentLevel) {
		this.indentLevel = indentLevel;
	}

	
}

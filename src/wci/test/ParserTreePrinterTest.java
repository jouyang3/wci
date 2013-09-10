package wci.test;

import org.junit.Test;

import wci.util.ParseTreePrinter;

public class ParserTreePrinterTest {

	@Test
	public void printWrapped(){
		String s = "I HATE MY \nJOB! CAN ANYONE SAVE ME?";
		String line = "     I LOVE THIS JOB!";
		ParseTreePrinter ptp = new ParseTreePrinter(System.out);
		ptp.setIndentLevel(5);
		ptp.setLine(new StringBuilder(line));
		ptp.setLength(line.length());
		ptp.setMaxLineWidth(8);
		ptp.append(s);
	}
	
}

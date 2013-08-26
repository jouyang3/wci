package wci.test;

import org.junit.Test;

import wci.frontend.pascal.PascalTokenType;
import wci.frontend.pascal.tokens.PascalNumberToken;
import static wci.util.Utility.*;

public class UtilityTest {
	
	@Test
	public void testHorner(){
		String numberText = "141516";
		System.out.println("Number value is "+ numerizeText(numberText, 10, true));
	}
	
	@Test
	public void testSomeEquals(){
		System.out.println(
				someEqual(Character.toString('+'), PascalTokenType.PLUS.getText(), PascalTokenType.MINUS.getText()));
	}

}

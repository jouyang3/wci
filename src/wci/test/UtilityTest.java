package wci.test;

import java.math.BigDecimal;

import org.junit.Test;
import static org.junit.Assert.*;

import wci.frontend.pascal.PascalTokenType;
import wci.frontend.pascal.tokens.PascalNumberToken;
import static wci.util.SexyAssistant.*;

public class UtilityTest {
	
	@Test
	public void testHorner(){
		String numberText = "12345";
		assertTrue(12345d == numerizeText(numberText, 10, false));
	}
	
	@Test
	public void testSomeEquals(){
		assertEquals(true, someEqual(Character.toString('+'), PascalTokenType.PLUS.getText(), PascalTokenType.MINUS.getText()));
	}
	
	@Test
	public void testDpPos(){
		String intText = "141516";
		assertEquals(0, dpPos(intText));
		
		String dbText = "1415.16";
		assertEquals(-2, dpPos(dbText));
		
		String eText = "5.1e6.5";
		assertEquals(5, dpPos(eText));
		
	}
	
	@Test
	public void testSigFig() throws Exception{
		String s1 = "03.1400";
		
		System.out.printf("UtilityTest.testSigFig(): %s has %d sigfigs.\n",s1,sigfig(s1));
		assertEquals(5, sigfig(s1));
		
		String s2 = "03.014";
		System.out.printf("UtilityTest.testSigFig(): %s has %d sigfigs.\n",s2,sigfig(s2));
		assertEquals(4, sigfig(s2));
		
		String s3 = "00.0014";
		System.out.printf("UtilityTest.testSigFig(): %s has %d sigfigs.\n",s3,sigfig(s3));
		assertEquals(2, sigfig(s3));
		
		String s4 = "0.0";
		System.out.printf("UtilityTest.testSigFig(): %s has %d sigfigs.\n",s4,sigfig(s4));
		assertEquals(1, sigfig(s4));
	}

}

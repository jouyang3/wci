package wci.frontend.pascal.tokens;

import static wci.frontend.pascal.PascalErrorCode.INVALID_NUMBER;
import static wci.frontend.pascal.PascalErrorCode.RANGE_INTEGER;
import static wci.frontend.pascal.PascalErrorCode.RANGE_REAL;
import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;
import static wci.frontend.pascal.PascalTokenType.ERROR;
import static wci.frontend.pascal.PascalTokenType.INTEGER;
import static wci.frontend.pascal.PascalTokenType.REAL;
import static wci.util.Utility.someEqual;

import java.math.BigDecimal;
import java.math.MathContext;

import wci.frontend.Source;
import wci.frontend.pascal.PascalToken;
import wci.frontend.pascal.PascalTokenType;
import wci.util.Utility;

public class PascalNumberToken extends PascalToken {
	
	public static Integer MAX_INTEGER = java.lang.Integer.MAX_VALUE;
	
	public static Integer MIN_INTEGER = java.lang.Integer.MIN_VALUE;
	
	public static Double MAX_REAL = java.lang.Double.MAX_VALUE+0.0;
	
	public static Double MIN_REAL = java.lang.Double.MIN_VALUE+0.0;

	public PascalNumberToken(Source source) throws Exception {
		super(source);
	}

	protected void extract() throws Exception {
		StringBuilder textBuffer = new StringBuilder();
		extractNumber(textBuffer);
		text = textBuffer.toString();
	}
	
	public java.math.BigDecimal d;

	protected void extractNumber(StringBuilder textBuffer) throws Exception {
		char currentChar = currentChar();
		type = INTEGER; // default to integer
		if (Character.isDigit(currentChar)
				|| someEqual(Character.toString(currentChar),
						PascalTokenType.PLUS.getText(), PascalTokenType.MINUS.getText())) {
			// before the decimal point
			String intPart = intPart(textBuffer);
			String dotPart = dotPart(textBuffer);
			String eParts[] = ePart(textBuffer);
			/*
			 * TODO: Implement the REAL | INTEGER number check
			 */
			if(type != ERROR){
				BigDecimal number = calculateValue(intPart, dotPart, eParts[0], eParts[1]);
				if(type == INTEGER){
					if(number.compareTo(new BigDecimal(MAX_INTEGER))>0 || 
							number.compareTo(new BigDecimal(MIN_INTEGER)) < 0 ){
						type = ERROR;
						value = RANGE_INTEGER;
					} else
					value = number.toBigInteger();
				} else if (type == REAL){
					if(number.compareTo(new BigDecimal(MAX_REAL))>0 || 
							number.compareTo(new BigDecimal(MIN_REAL)) < 0 ){
						type = ERROR;
						value = RANGE_REAL;
					} else
					value = number;
				} else {
					type = ERROR;
					value = INVALID_NUMBER;
				}
			}
		} else {
			type = ERROR;
			value = UNEXPECTED_TOKEN;
		}
	}

	private String skipAllDigits() throws Exception {
		char currentChar = currentChar();
		StringBuilder intPartBuffer = new StringBuilder();
		do {
			intPartBuffer.append(currentChar);
		} while (Character.isDigit((currentChar = nextChar())));
		return intPartBuffer.toString();
	}

	private String intPart(StringBuilder textBuffer) throws Exception {
		char currentChar = currentChar();
		String signedIntPart = "";
		if (someEqual(Character.toString(currentChar),
				PascalTokenType.PLUS.getText(), PascalTokenType.MINUS.getText())) {
			textBuffer.append(currentChar);
			signedIntPart += currentChar;
			currentChar = nextChar(); // consumes +/-
			if (!Character.isDigit(currentChar)) {
				textBuffer.append(currentChar); //consumes the error character
				type = ERROR;
				value = INVALID_NUMBER;
				return null;
			}
		}
		if (!Character.isDigit(currentChar) && !Character.isWhitespace(currentChar)){
			type = ERROR;
			value = INVALID_NUMBER;
			nextChar(); //consumes the error character
			return null;
		}
		String intPart = skipAllDigits();
		signedIntPart += intPart;
		textBuffer.append(intPart);
		return signedIntPart;
	}

	private String dotPart(StringBuilder textBuffer) throws Exception {
		Character currentChar = currentChar();
		if (currentChar.toString().equals(PascalTokenType.DOT.getText())) {
			textBuffer.append(currentChar); 
			currentChar = nextChar();//consumes dot
			if (Character.isDigit(currentChar)) {
				type = REAL;
				String dotPart = skipAllDigits();
				textBuffer.append(dotPart);
				return dotPart;
			}
			// appends the error character
			if(!Character.isWhitespace(currentChar)){
				textBuffer.append(currentChar);
				nextChar();//consumes the error character
			}
			type = ERROR;
			value = INVALID_NUMBER;
			return null;
		}
		return null;
	}

	private String[] ePart(StringBuilder textBuffer) throws Exception {
		try {
			Character currentChar = currentChar();
			if (currentChar.toString().equalsIgnoreCase("e")) {
				type = REAL;
				textBuffer.append(currentChar); 
				currentChar = nextChar(); //consumes e
				StringBuilder eTextBuffer = new StringBuilder();
				/*
				 * Because it is the second time that we've called intPart,
				 * if encountering a single digit integer, the character has
				 * already been consumed! We cannot judge whether the integer
				 * is valid or not based simply after E. Hence, we will check
				 * the character before we call the int part method.
				 */
				if(!Character.isDigit(currentChar) && !someEqual(Character.toString(currentChar),
						PascalTokenType.PLUS.getText(), PascalTokenType.MINUS.getText())){
					if(!Character.isWhitespace(currentChar))
						textBuffer.append(currentChar);
					type = ERROR;
					value = INVALID_NUMBER;
					nextChar(); //consumes the error character
					return new String[]{ null, null };
				}
				// builds decimal
				String intPart = intPart(eTextBuffer);
				String dotPart = dotPart(eTextBuffer);
				textBuffer.append(eTextBuffer);
				return new String[] { intPart, dotPart };
			}
			return new String[] { null, null };
		} catch (Exception ex) {
			type = ERROR;
			value = INVALID_NUMBER;
			return new String[] { null, null };
		}
	}

	private BigDecimal calculateValue(String intPart, String dotPart,
			String eIntPart, String eDotPart) throws Exception{
		int base = 10;
		if (intPart == null)
			intPart = "0";
		if (dotPart == null)
			dotPart = "0";
		if (eIntPart == null)
			eIntPart = "0";
		if (eDotPart == null)
			eDotPart = "0";
		
		//10^(intPart.dotPart) = 10^(intPart)*10^(0.dotPart)
		//calculates fraction part value
		BigDecimal exp10Frac = new BigDecimal(Math.pow(base, new Double("0."+eDotPart)));
		BigDecimal exp10Int = new BigDecimal(Math.pow(base, new Double(eIntPart)));
		BigDecimal exp = exp10Int.multiply(exp10Frac);
		BigDecimal unscaled = new BigDecimal(intPart+"."+dotPart);
		
		BigDecimal val = unscaled.multiply(exp);
		//calculates precision
		int precision = Utility.sigfig(intPart+"."+dotPart);
		System.out.println("PascalNumberToken.calculateValue(): Precision = "+precision);
		
		//now set rounding mode 
		
		val = val.round(new MathContext(precision));
		
		return val;
	}

}

package wci.frontend.pascal.tokens;

import static wci.frontend.pascal.PascalErrorCode.INVALID_NUMBER;
import static wci.frontend.pascal.PascalErrorCode.UNEXPECTED_TOKEN;
import static wci.frontend.pascal.PascalTokenType.ERROR;
import static wci.frontend.pascal.PascalTokenType.INTEGER;
import static wci.frontend.pascal.PascalTokenType.REAL;
import wci.frontend.Source;
import wci.frontend.pascal.PascalToken;
import wci.frontend.pascal.PascalTokenType;
import static wci.util.Utility.*;

public class PascalNumberToken extends PascalToken {

	public PascalNumberToken(Source source) throws Exception {
		super(source);
	}

	protected void extract() throws Exception {
		StringBuilder textBuffer = new StringBuilder();
		extractNumber(textBuffer);
		text = textBuffer.toString();
	}

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
			if(type != ERROR)
				value = calculateValue(intPart, dotPart, eParts[0], eParts[1]);
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

	private double calculateValue(String intPart, String dotPart,
			String eIntPart, String eDotPart) {
		int base = 10;
		if (intPart == null)
			intPart = "0";
		if (dotPart == null)
			dotPart = "0";
		if (eIntPart == null)
			eIntPart = "0";
		if (eDotPart == null)
			eDotPart = "0";
		double exp = numerizeText(eIntPart, base, false)
				+ numerizeText(eDotPart, base, true);
		return (numerizeText(intPart, base, false) + numerizeText(dotPart,
				base, true)) * Math.pow(base, exp);
	}

}

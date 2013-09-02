package wci.util;

import java.math.BigDecimal;

import wci.frontend.pascal.PascalTokenType;

public class SexyAssistant {
	
	public static boolean someEqual(Object value, Object...objs){
		for(Object obj:objs){
			if(obj.equals(value))
				return true;
		}
		return false;
	}
	
	public static int sigfig(String simpleNumberText) throws Exception{
		if(simpleNumberText==null || simpleNumberText == "") 
			throw new IllegalArgumentException(String.format("The supplied simpleNumberText ['%s'] is empty.", simpleNumberText));
		if(simpleNumberText.indexOf('e')>-1)
			throw new IllegalArgumentException(String.format("The supplied simpleNumberText ['%s'] cannot contain exponential component.", simpleNumberText));
		//test if number is valid
		if((new BigDecimal(simpleNumberText)).compareTo(new BigDecimal(0)) == 0){
			return simpleNumberText.length() - simpleNumberText.indexOf('.') -1;
		}
		int sigfigs = 0;
		//remove prefix zero's
		int i;
		boolean zeroPrefix = false;
		for(i=0;i<simpleNumberText.length(); i++){
			Character digit = simpleNumberText.charAt(i);
			if(PascalTokenType.DOT.getText().equals(digit.toString())
					|| PascalTokenType.PLUS.getText().equals(digit.toString())
					|| PascalTokenType.MINUS.getText().equals(digit.toString()))
				continue; //skips dot
			if(Character.isDigit(digit)){
				if(!zeroPrefix && digit != '0'){
					zeroPrefix = true; //counting starts whenever first non-zero is encountered.
				}
				if(zeroPrefix){
					sigfigs++;
				}
			} else
				throw new IllegalArgumentException(String.format("The supplied simpleNumberText ['%s'] contains not-text characters.",simpleNumberText));
		}
		
		return sigfigs;
		
		
	}
	
	/**
	 * Returns the decimal point position of a number string.
	 * TODO: Test it!
	 * @param numberString
	 * @return
	 */
	public static int dpPos(String numberString){
		// 0.001e6, dotPos = 1, length = 7, decPos = 3 = 
		int dotPos = numberString.indexOf('.');
		int expPos = numberString.length();
		if( dotPos==-1 ){ //no deciaml point
			dotPos = numberString.length();
			expPos = numberString.length() + 1;
		}
		Integer eInt = 0; //10^0 = 1
		int tempExpPos = 0;
		if((tempExpPos = numberString.indexOf('e')) > -1 || (tempExpPos = numberString.indexOf('E')) > -1){
			expPos = tempExpPos;
			//extracts the number after e
			String expString = numberString.substring(expPos + 1);
			int expStrDotPos = expString.indexOf('.');
			//extracts integer part of the exponent
			String eIntStr = expString.substring(0,expStrDotPos>-1?expStrDotPos:expString.length());
			eInt = Integer.parseInt(eIntStr);
		}
		int decPos = -1*(expPos-dotPos-1);
		/*
		 * REALS: 3.14159e10, val = 31415900000 ,dpForPureInt = 10 - 5 = 5
		 * 
		 * INT: 3145, val = 3145, dpForPureInt = 0
		 */
		return eInt + decPos;
	}
	
	
	public static boolean lessThan(String numberString, String max){
		int dpNumPos = dpPos(numberString);
		if(dpNumPos >= dpPos(max))
			return false;
		//now we check digit-by-digit
		for(int i = 0; i < numberString.length(); i++){
			Character nc = numberString.charAt(i);
			Character mc = max.charAt(i);
			
		}
		return false;
	}
	
	public static double numerizeText(String numberText, int base, boolean afterDot)
			throws IllegalArgumentException {
		if (numberText != null)
			numberText = numberText.replaceAll(" ", "");
		if (numberText == null || numberText.length() < 1) {
			throw new IllegalArgumentException("The supplied text is empty.");
		}
		/**
		 * 5151 = 5*1000 + 1*100 + 5*10 + 1 = (5*100+1*10+5)*10 + 1
		 * = ((5*10+1)*10+5)*10 + 1
		 */
		/**
		 * 0.5151 = 5*0.1 + 1*0.01 + 5*0.001 + 1*0.0001 = 5/10+1/10/10+5/10/10/10+1/10/10/10/10
		 * = (5+1/10+5/10/10+1/10/10/10)/10 = (5+(1+5/10+1/10/10)/10)/10 = (5+(1+(5+1/10)/10)/10)/10
		 */
		// Horner's algorithm.
		char firstChar = numberText.charAt(0);
		double sign = 1d;
		if(firstChar == '+'){
			numberText = numberText.substring(1,numberText.length());
		} else if(firstChar == '-'){
			numberText = numberText.substring(1, numberText.length());
			sign = -1d;
		}
		numberText="0"+numberText;
		return sign*horner(numberText, base, 0, afterDot, 0);
	}
	
	private static double horner(String numberText, int base, int i, boolean afterDot, double sum){
		if(afterDot){
			if(i == numberText.length())
				return 0;
			return extractDigit(numberText, i++)+horner(numberText, base, i, afterDot, sum)/base;
		} else {
			if(i == numberText.length())
				return sum;
			sum = sum*base + extractDigit(numberText, i);
			return horner(numberText, base, ++i, afterDot, sum);
		}
	}

	/**
	 * Extract (left-to-right) Digits of a number string.
	 * @param numberText
	 * @param pos
	 * @return
	 */
	public static double extractDigit(String numberText, int pos) {
		if (numberText.length() < 1) {
			throw new IllegalArgumentException("The supplied text is empty.");
		}
		if (numberText.length() - 1 < pos)
			throw new IllegalArgumentException(String.format(
					"The supplied position('%d') exceeds or equals the length('%d') of"
							+ " the text('%s').", pos, numberText.length(),
					numberText));
		Character c = numberText.charAt(pos);
		return Double.parseDouble(c.toString());
	}
	
}

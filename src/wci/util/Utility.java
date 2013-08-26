package wci.util;

public class Utility {
	
	public static boolean someEqual(Object value, Object...objs){
		for(Object obj:objs){
			if(obj.equals(value))
				return true;
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

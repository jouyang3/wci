package wci.frontend.pascal.tokens;

import wci.frontend.Source;
import wci.frontend.pascal.PascalErrorCode;
import wci.frontend.pascal.PascalToken;

import static wci.frontend.pascal.PascalTokenType.ERROR;

public class PascalErrorToken extends PascalToken {

	public PascalErrorToken(Source source, PascalErrorCode errorCode,
			String tokenText) throws Exception {
		super(source);

		this.text = tokenText;
		this.type = ERROR;
		this.value = errorCode;
	}
	
	/**
	 * Do nothing. Do not consume any source characters.
	 */
	protected void extract() throws Exception{
	}

}

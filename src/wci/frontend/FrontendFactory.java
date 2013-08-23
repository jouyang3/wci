package wci.frontend;

import wci.frontend.pascal.PascalParserTD;
import wci.frontend.pascal.PascalScanner;

public class FrontendFactory {

	/**
	 * 
	 * @param language "Pascal"
	 * @param type "top-down" | "bottom-up"
	 * @param source Source object representing the source file.
	 * @return
	 * @throws Exception
	 */
	public static Parser createParser(String language, String type, Source source) throws Exception{
		if(language.equalsIgnoreCase("Pascal")){
			if(type.equalsIgnoreCase("top-down")){
				Scanner scanner = new PascalScanner(source);
				return new PascalParserTD(scanner);
			}
			throw new Exception("Parser Factory: Invalid Pascal Parser Type '"+type+"'");
		} else {
			throw new Exception("Parser Factory: Invalid Language '"+language+"'");
		}
	}
	
	
}

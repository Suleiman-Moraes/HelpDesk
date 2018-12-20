package br.com.senaigo.helpdesk.api.util;

/**
 * 
 * @author suleiman-am
 *
 */
public class StringUtil {
	public static Boolean isNotNullOrEmpty(String text) {
		return isNotNull(text) && isNotEmpty(text);
	}
	
	public static Boolean isNullOrEmpty(String text) {
		return !isNotNullOrEmpty(text);
	}
	
	public static Boolean isNotNull(String text) {
		return text != null;
	}
	
	public static Boolean isNotEmpty(String text) {
		return !text.trim().isEmpty() && !text.trim().equals("");
	}
	
	public static Boolean isEmpty(String text) {
		return !isNotEmpty(text);
	}
	
	public static Boolean isNull(String text) {
		return !isNotNull(text);
	}
}

package utils;

public class Strings {
	private static final String SCRIPT_NAME = "Cape's Account Creator";
	private static final String CREATE_ACCOUNT_URL = "https://secure.runescape.com/m=account-creation/g=oldscape/create_account";
	private static final String WEBSITE_KEY = "6LccFA0TAAAAAHEwUJx_c1TfTBWMTAOIphwTtd1b";
	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116";
	private static final String BASE_URL = CREATE_ACCOUNT_URL + "?trialactive=true&onlyOneEmail=1&displayname_present=true";
	
	private Strings() {
	}
	
	public static String getScriptName() {
		return SCRIPT_NAME;
	}
	
	public static String getAccountCreateURL() {
		return CREATE_ACCOUNT_URL;
	}
	
	public static String getWebsiteKey() {
		return WEBSITE_KEY;
	}
	
	public static String getUserAgent() {
		return USER_AGENT;
	}
	
	/**
	 * Creates the URL used to create the account with the given details.
	 */
	public static String createURL(int age, String email, String displayName, String password, String captcha) {
		return BASE_URL + String.format("&age=%d&displayname=%s&email1=%s&password1=%s&password2=%s&g-recaptcha-response=%s&submit=Join Now", 
				age, displayName, email, password, password, captcha);
	}
	
	public static String createURL(String displayName, String password, String captcha) {
		return createURL(Random.random(15, 36), Email.generateEmail(displayName), displayName, password, captcha);
	}
}

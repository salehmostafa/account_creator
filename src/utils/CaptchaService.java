package utils;

public enum CaptchaService {
	ANTI_CAPTCHA("Anti-Captcha"),
	TWOCAPTCHA("2Captcha");
	
	private final String toString;
	
	private CaptchaService(String toString) {
		this.toString = toString;
	}
	
	@Override
	public String toString() {
		return toString;
	}
}

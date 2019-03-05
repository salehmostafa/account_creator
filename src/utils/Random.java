package utils;

public class Random {
	
	private Random() {
	}
	
	public static int random(int min, int unreachableMax) {
		return min + (int)(Math.random()*(unreachableMax - min));
	}
	
	public static int random(int unreachableMax) {
		return random(0, unreachableMax);
	}
	
	public static char randomLetter() {
		return (char)('a' + random(26));
	}
	
	public static char randomCharredNumber() {
		return (char)('0' + random(10));
	}
	
	public static String randomString(int length) {
		String strung = "";
		for(int i = 0; i < length; i++)
			strung += Random.randomLetter();
		return strung;
	}
}

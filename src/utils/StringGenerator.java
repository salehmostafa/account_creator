package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import main.Logger;

public class StringGenerator {
	private final int min, max;
	
	private List<String> possibilities;
	
	private final String oneReturn;
	
	public StringGenerator(int min, int max, String generating, String filePath) {
		this.min = min;
		this.max = max;
		
		if(filePath != null) {
			File file = new File(filePath);
			if(file.exists()) {
				try {
					possibilities = Files.readAllLines(file.toPath());
					possibilities.removeIf(i -> i == null || i.length() >= max - 1);
					if(possibilities.isEmpty()) {
						possibilities = null;
						Logger.addMessage("No valid words present in file to build " + generating + "s");
					}
				} catch(IOException ioe) {
					possibilities = null;
					Logger.addMessage("Error attempting to access file for " + generating + "s");
				}
			} else {
				possibilities = null;
				Logger.addMessage("Nonexistent file given to build strings for " + generating + "s");
			}
		}
		this.oneReturn = null;
	}
	
	public StringGenerator(String oneReturn) {
		this.min = -1;
		this.max = -1;
		this.possibilities = null;
		this.oneReturn = oneReturn;
	}
	
	public String generateString() {
		if(oneReturn != null)
			return oneReturn;
		if(possibilities == null || possibilities.size() == 0)
			return Random.randomString(Random.random(min, max + 1));
		
		String word = possibilities.get(Random.random(possibilities.size()));
		if(Math.random() < 0.8)
			word = word.substring(0, 1).toUpperCase() + word.substring(1);
		if(possibilities.size() >= 5) {//min = 6, max = 12, word = "hello" options = "bye","fatty","pumpkin"
			int randomLen = Random.random(2, 5); //rL = 3
			int startLen = word.length(); //sL = 6
			List<String> values = Filter.getMatches(possibilities, s -> startLen + s.length() <= max - randomLen);
			if(values.size() >= 3) {
				String generatedString = values.get(Random.random(values.size()));
				if(Math.random() < 0.333)
					generatedString = generatedString.substring(0, 1).toUpperCase() + generatedString.substring(1);
				word += generatedString; //making final length <= max - (2-5)
			}
			//System.out.println(word);
		}
		//value here will be random(min OR at the most max - 1, max), always adds at least 1 char
		int finalLength = Random.random(min > word.length() ? min : (word.length() + 1), max + 1);
		for(int i = word.length(); i < finalLength; i++) {
			word += (Math.random() < 0.5 ? Random.randomLetter() : Random.randomCharredNumber());
		}
		return word;
	}
}

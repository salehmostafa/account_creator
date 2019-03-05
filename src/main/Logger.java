package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Logger {
	private static JTextArea logger, accountLogger;
	
	private static File accountLogFile;
	
	
	private static final List<String> cachedMessages = new ArrayList<>();
	
	private Logger() {
	}
	
	public static void setup(JTextArea logger, JTextArea accountLogger, boolean logToFile, String filePath) {
		Logger.logger = logger;
		Logger.accountLogger = accountLogger;
		
		if(logToFile && filePath != null) {
			accountLogFile = new File(filePath);
			try {
				if(!accountLogFile.exists() && !accountLogFile.createNewFile()) {
					accountLogFile = null;
				}
			} catch (IOException e) {
				accountLogFile = null;
				e.printStackTrace();
			}
		} else {
			accountLogFile = null;
		}
		
		while(cachedMessages.size() > 0) {
			addMessage(cachedMessages.remove(0));
		}
	}
	
	public static void addMessage(String message) {
		if(logger != null) {
			String text = String.format("[%s]: %s\n", DateFormat.getDateTimeInstance().format(new Date()), message);
			SwingUtilities.invokeLater(() -> logger.append(text));
		} else {
			cachedMessages.add(message);
		}
	}
	
	public static void addMessage(String string, Object... formattables) {
		addMessage(String.format(string, formattables));
	}
	
	public static void addAccount(String account) {
		if(accountLogger != null)
			SwingUtilities.invokeLater(() -> accountLogger.setText(accountLogger.getText() + account + "\n"));
		if(accountLogFile != null) {
			addMessage("Adding account to file...");
			addToFile(account);
		}
	}
	
	private static void addToFile(String account) {
		try {
			List<String> lines = Files.readAllLines(accountLogFile.toPath());
			lines.add(account);
			Files.write(accountLogFile.toPath(), lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

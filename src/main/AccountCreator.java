package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.anti_captcha.Api.NoCaptcha;
import com.anti_captcha.Api.NoCaptchaProxyless;
import com.anti_captcha.Api.NoCaptcha.ProxyTypeOption;
import com.twocaptcha.api.ProxyType;
import com.twocaptcha.api.TwoCaptchaService;

import utils.*;

public class AccountCreator {
	private final String KEY;
	private final CaptchaService service;
	private int counter = 0;
	private long lastCreationTime = 0;
	
	public AccountCreator(String KEY, CaptchaService service) {
		this.KEY = KEY;
		this.service = service;
	}
	
	public boolean createAccount(String username, String password, UserProxy proxy) {
		Logger.addMessage("Trying Username: %s Password: %s", username, password);
		
		long start = System.currentTimeMillis();
		String solution;
		try {
			solution = getCaptcha(proxy);
		} catch (MalformedURLException | InterruptedException e) {
			Logger.addMessage(e.getMessage());
			solution = null;
		}
		if(solution == null) {
			Logger.addMessage("Error finding captcha message! Fail count: " + (++counter));
			return false;
		}
		
		Logger.addMessage("Successfully found captcha code! Time used: " + (System.currentTimeMillis() - start) + "ms");
		String email = Email.generateEmail(username);
		String url = Strings.createURL(Random.random(14, 30), email, username, password, solution);
		
		URLConnection connection;
		try {
			connection = generateConnection(url, proxy);
		} catch (IOException e) {
			Logger.addMessage(e.getMessage());
			Logger.addMessage("Failed to create connection! Fail count: " + (++counter));
			return false;
		}
		
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			Logger.addMessage(e.getMessage());
			Logger.addMessage("Failed to create input stream! Fail count: " + (++counter));
			return false;
		}
		
		String line;
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains("Account Created")) {
					Logger.addAccount(email + ":" + password);
					Logger.addMessage("Account successfully created!");
					br.close();
					lastCreationTime = System.currentTimeMillis() - start;
					return true;
				}
			}
		} catch (IOException e) {
			Logger.addMessage(e.getMessage());
			Logger.addMessage("Failed to read result page! Fail Count: " + (++counter));
			return false;
		}
		Logger.addMessage("Unsuccessful account creation. Fail Count: " + (++counter));
		return false;
	}
	
	private String getCaptcha(UserProxy proxy) throws InterruptedException, MalformedURLException {
		if(service == CaptchaService.ANTI_CAPTCHA) {
			NoCaptchaProxyless solver;
			if(proxy != null) {
				Logger.addMessage("Finding anti-captcha solution with proxy %s", proxy.toString());
				NoCaptcha tempSolver = new NoCaptcha();
				tempSolver.setUserAgent(Strings.getUserAgent());
				tempSolver.setProxyAddress(proxy.getIP());
				tempSolver.setProxyPort(proxy.getPort());
				tempSolver.setProxyType(ProxyTypeOption.HTTP);
			
				if(proxy.shouldEnterUserAndPass()) {
					tempSolver.setProxyLogin(proxy.getUsername());
					tempSolver.setProxyPassword(proxy.getPassword());
				}
				solver = tempSolver;
			} else {
				Logger.addMessage("Finding captcha solution without proxy");
				solver = new NoCaptchaProxyless();
			}
			solver.setWebsiteUrl(new URL(Strings.getAccountCreateURL()));
			solver.setClientKey(KEY);
			solver.setWebsiteKey(Strings.getWebsiteKey());
		
			if(solver.createTask() && solver.waitForResult(60000)) {
				return solver.getTaskSolution();
			}
			Logger.addMessage(solver.getErrorMessage());
			return null;
		}
		if(service == CaptchaService.TWOCAPTCHA) {
			TwoCaptchaService service = new TwoCaptchaService(KEY, Strings.getWebsiteKey(), Strings.getAccountCreateURL());
			if(proxy != null) {
				Logger.addMessage("Finding 2Captcha solution with proxy %s", proxy.toString());
				service.setProxyIp(proxy.getIP());
				service.setProxyPort("" + proxy.getPort());
				if(proxy.shouldEnterUserAndPass()) {
					service.setProxyUser(proxy.getUsername());
					service.setProxyPw(proxy.getPassword());
				}
				service.setProxyType(ProxyType.HTTP);
			}
			try {
				String responseToken = service.solveCaptcha(true);
				Logger.addMessage("The response token is: " + responseToken);
				return responseToken;
			} catch (InterruptedException e) {
				Logger.addMessage("2Captcha: ERROR case 1");
				e.printStackTrace();
			} catch (IOException e) {
				Logger.addMessage("2Captcha: ERROR case 2");
				e.printStackTrace();
			} catch (Exception e) {
				Logger.addMessage("Error obtaining captcha value!");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static URLConnection generateConnection(String url, UserProxy proxy) throws MalformedURLException, IOException {
		return proxy == null ? new URL(url).openConnection() : new URL(url).openConnection(proxy.asJavaProxy());
	}
	
	public int getFailCount() {
		return counter;
	}
	
	public void resetFailCounter() {
		counter = 0;
	}
	
	public long getLastCreationTime() {
		return lastCreationTime;
	}
}

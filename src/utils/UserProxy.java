package utils;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class UserProxy {
	private final String ip, username, password;
	private final int port;
	
	public UserProxy(String ip, int port, String username, String password) {
		this.ip = ip;
		this.port = port;
		
		this.username = username;
		this.password = password;
	}
	
	public UserProxy(String ip, int port) {
		this(ip, port, null, null);
	}
	
	@Override
	public String toString() {
		return getIP() + ":" + getPort();
	}
	
	public String getIP() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean shouldEnterUserAndPass() {
		return getUsername() != null && getPassword() != null;
	}
	
	/**
	 * Returns the UserProxy as an HTTP java.net.Proxy
	 */
	public Proxy asJavaProxy() {
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getIP(), getPort()));
	}
	
	public static UserProxy parseProxy(String details) {
		String[] splitDetails = details.split(":");
		int port;
		try {
			port = Integer.parseInt(splitDetails[1].trim());
		} catch(Exception e) {
			return null;
		}
		if(splitDetails.length >= 4) {
			return new UserProxy(splitDetails[0].trim(), port, splitDetails[2].trim(), splitDetails[3].trim());
		} else if(splitDetails.length >= 2) {
			return new UserProxy(splitDetails[0].trim(), port);
		}
		return null;
	}
}

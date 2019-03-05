package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import utils.*;

public class GUI extends JFrame {

	private static final long serialVersionUID = 9214954297969780069L;
	private boolean started = false;
	private JPanel contentPane, proxyPanel, accountPanel, captchaPanel;
	
	//accountPanel
	private JTextField userFilePath, passFilePath, accountLogPath, thePass, accountCount;
	private JCheckBox listUsernames, listPasses, singularPassword, logAccounts;
	
	//proxyPanel
	private JCheckBox enableProxies, linearRotation;
	private JTextArea proxyList;
	
	private JTextField captchaKey;

	public GUI() {
		setTitle(Strings.getScriptName());
		setResizable(false);
		setBounds(100, 100, 550, 490);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		proxyPanel = new JPanel();
		proxyPanel.setLayout(null);
		proxyPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Proxy Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		proxyPanel.setBounds(6, 6, 260, 306);
		contentPane.add(proxyPanel);
		
		captchaPanel = new JPanel();
		captchaPanel.setLayout(null);
		captchaPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "2Captcha & Data Saving", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		captchaPanel.setBounds(6, 320, 535, 93);
		contentPane.add(captchaPanel);
		
		JTextArea txtrPersonalCaptchaKey = new JTextArea();
		txtrPersonalCaptchaKey.setEditable(false);
		txtrPersonalCaptchaKey.setText("Personal Captcha Key:");
		txtrPersonalCaptchaKey.setBackground(contentPane.getBackground());
		txtrPersonalCaptchaKey.setBounds(16, 28, 138, 16);
		captchaPanel.add(txtrPersonalCaptchaKey);
		
		captchaKey = new JTextField();
		captchaKey.setBounds(166, 22, 351, 28);
		captchaPanel.add(captchaKey);
		captchaKey.setColumns(10);
		
		JCheckBox saveData = new JCheckBox("Save Data for Next Time");
		saveData.setBounds(47, 56, 191, 23);
		captchaPanel.add(saveData);
		
		JButton btnLoadSavedData = new JButton("Load Saved Data");
		btnLoadSavedData.setBounds(264, 55, 204, 29);
		btnLoadSavedData.addActionListener(l -> loadData());
		captchaPanel.add(btnLoadSavedData);
		
		enableProxies = new JCheckBox("Enable Proxy Usage");
		enableProxies.setBounds(15, 25, 159, 23);
		proxyPanel.add(enableProxies);
		
		JTextArea proxyInstructions = new JTextArea();
		proxyInstructions.setEditable(false);
		proxyInstructions.setText("Copy and Paste Your Proxies: ");
		proxyInstructions.setBackground(contentPane.getBackground());
		proxyInstructions.setBounds(37, 90, 187, 15);
		proxyPanel.add(proxyInstructions);
		
		//String ip, int port, String username, String password
		proxyList = new JTextArea("100.100.100:43975,\n123.456.789:12345,\n641.444.444:44321");
		proxyList.setForeground(Color.GRAY);
		proxyList.addFocusListener(generateDefaultTextListener(proxyList));
		
		JScrollPane scrollPane = new JScrollPane(proxyList);
		scrollPane.setBounds(17, 115, 223, 176);
		proxyPanel.add(scrollPane);
		
		linearRotation = new JCheckBox("Go through proxies linearly");
		linearRotation.setBounds(15, 55, 206, 23);
		proxyPanel.add(linearRotation);
		
		accountPanel = new JPanel();
		accountPanel.setLayout(null);
		accountPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Account Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		accountPanel.setBounds(280, 6, 260, 306);
		contentPane.add(accountPanel);
		
		listUsernames = new JCheckBox("Generate usernames from list");
		listUsernames.setBounds(16, 62, 236, 23);
		accountPanel.add(listUsernames);
		
		listPasses = new JCheckBox("Generate passwords from list");
		listPasses.setBounds(16, 120, 236, 23);
		accountPanel.add(listPasses);
		
		singularPassword = new JCheckBox("Use one password for all");
		singularPassword.setBounds(16, 178, 223, 23);
		accountPanel.add(singularPassword);
		
		userFilePath = new JTextField("File path for usernames");
		userFilePath.setEnabled(false);
		userFilePath.setForeground(Color.GRAY);
		userFilePath.addFocusListener(generateDefaultTextListener(userFilePath));
		userFilePath.setBounds(23, 92, 223, 23);
		accountPanel.add(userFilePath);
		userFilePath.setColumns(10);
		
		passFilePath = new JTextField("File path for passwords");
		passFilePath.setEnabled(false);
		passFilePath.setForeground(Color.GRAY);
		passFilePath.addFocusListener(generateDefaultTextListener(passFilePath));
		passFilePath.setColumns(10);
		passFilePath.setBounds(23, 150, 223, 23);
		accountPanel.add(passFilePath);
		
		accountLogPath = new JTextField("File path for accounts");
		accountLogPath.setEnabled(false);
		accountLogPath.setForeground(Color.GRAY);
		accountLogPath.addFocusListener(generateDefaultTextListener(accountLogPath));
		accountLogPath.setColumns(10);
		accountLogPath.setBounds(23, 266, 223, 23);
		accountPanel.add(accountLogPath);
		
		logAccounts = new JCheckBox("Log accounts to file");
		logAccounts.setBounds(16, 236, 223, 23);
		accountPanel.add(logAccounts);
		
		thePass = new JTextField("Password");
		thePass.setEnabled(false);
		thePass.setForeground(Color.GRAY);
		thePass.addFocusListener(generateDefaultTextListener(thePass));
		thePass.setColumns(10);
		thePass.setBounds(23, 208, 223, 23);
		accountPanel.add(thePass);
		
		JTextArea txtrNumberOfAccounts = new JTextArea();
		txtrNumberOfAccounts.setEditable(false);
		txtrNumberOfAccounts.setText("Number of accounts to make:");
		txtrNumberOfAccounts.setBackground(contentPane.getBackground());
		txtrNumberOfAccounts.setBounds(17, 34, 188, 16);
		accountPanel.add(txtrNumberOfAccounts);
		
		accountCount = new JTextField();
		accountCount.setBounds(207, 28, 45, 28);
		accountPanel.add(accountCount);
		accountCount.setColumns(10);
		
		GUI frame = this;
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
		        if(JOptionPane.showConfirmDialog(frame, String.format("Are you sure to close %s?", Strings.getScriptName()), 
		        		Strings.getScriptName(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
		        	System.exit(0);
		        }
			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});
		
		JButton btnBeginCreatingAccounts = new JButton("Begin Creating Accounts");
		btnBeginCreatingAccounts.setBounds(6, 425, 538, 29);
		btnBeginCreatingAccounts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.addMessage("Completed GUI!");
				frame.dispose();
				frame.setVisible(false);
				started = true;
				if(saveData.isSelected())
					saveData();
			}
		});
		contentPane.add(btnBeginCreatingAccounts);
		
		setVisible(true);
		attach(listUsernames, userFilePath);
		attach(listPasses, passFilePath);
		attach(singularPassword, thePass);
		attach(logAccounts, accountLogPath);
	}
	
	private String getCaptchaKey() {
		return captchaKey.getText();
	}
	
	private boolean useProxies() {
		return enableProxies.isSelected();
	}
	
	private boolean useProxiesLinearly() {
		return linearRotation.isSelected();
	}
	
	private int accountCount() {
		int count;
		try {
			count = Integer.parseInt(accountCount.getText().trim());
		} catch (Exception e) {
			Logger.addMessage("Unable to parse number of accounts! Using default 500!");
			count = 500;
		}
		return count;
	}
	
	private boolean usersFromList() {
		return listUsernames.isSelected();
	}
	
	private boolean passesFromList() {
		return listPasses.isSelected();
	}
	
	private boolean onePass() {
		return singularPassword.isSelected();
	}
	
	private List<UserProxy> getProxies() {
		List<UserProxy> proxies = new ArrayList<>();
		String[] strungProxies = proxyList.getText().split(",");
		for(String strungProxy : strungProxies) {
			UserProxy addable = UserProxy.parseProxy(strungProxy);
			if(addable != null)
				proxies.add(addable);
			else
				Logger.addMessage("Unable to add proxy: " + strungProxy);
		}
		return proxies;
	}
	
	public boolean shouldLogAccounts() {
		return logAccounts.isSelected();
	}
	
	public boolean getStarted() {
		return started;
	}
	
	public CreationHandler getHandler() {
		return new CreationHandler(CaptchaService.TWOCAPTCHA, accountCount(), getCaptchaKey(), 
				shouldLogAccounts(), accountLogPath.getText(), useProxies(), useProxiesLinearly(), getProxies(),
				new StringGenerator(7, 12, "username", usersFromList() ? userFilePath.getText() : null),
				(!onePass() ? new StringGenerator(10, 18, "password", passesFromList() ? passFilePath.getText() : null) :
				new StringGenerator(thePass.getText())));
	}
	
	private FocusListener generateDefaultTextListener(JTextComponent field) {
		return new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if(field.getForeground().equals(Color.GRAY)) {
					field.setForeground(Color.BLACK);
					field.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
			}
		};
	}
	
	private void attach(JCheckBox checkbox, JTextField field) {
		checkbox.addChangeListener(l -> field.setEnabled(checkbox.isSelected()));
	}
	
	public void saveData() {
		OutputStream output = null;
		try {
			Properties properties = new Properties();
			output = new FileOutputStream("accountcreator.properties");
			
			if(usersFromList()) {
				properties.setProperty("custom_users", "true");
				properties.setProperty("user_file_path", userFilePath.getText().trim());
			} else {
				properties.setProperty("custom_users", "false");
				properties.setProperty("user_file_path", "UNKNOWN");
			}
			if(passesFromList()) {
				properties.setProperty("custom_passes", "true");
				properties.setProperty("pass_file_path", passFilePath.getText().trim());
			} else {
				properties.setProperty("custom_passes", "false");
				properties.setProperty("pass_file_path", "UNKNOWN");
			}
			
			if(onePass()) {
				properties.setProperty("one_pass", "true");
				properties.setProperty("singular_password", thePass.getText().trim());
			} else {
				properties.setProperty("one_pass", "false");
				properties.setProperty("singular_password", "UNKNOWN");
			}
			
			if(shouldLogAccounts()) {
				properties.setProperty("log_accounts", "true");
				properties.setProperty("account_log_path", accountLogPath.getText().trim());
			} else {
				properties.setProperty("log_accounts", "false");
				properties.setProperty("account_log_path", "UNKNOWN");
			}
			
			properties.setProperty("accounts_to_make", "" + accountCount());
			properties.setProperty("captcha_key", getCaptchaKey());
			
			if(useProxies()) {
				properties.setProperty("use_proxies", "true");
				properties.setProperty("proxy_list", proxyList.getText().trim());
			} else {
				properties.setProperty("use_proxies", "false");
				properties.setProperty("proxy_list", "UNKNOWN");
			}
			
			properties.setProperty("use_proxies_linearly", useProxiesLinearly() ? "true" : "false");
		
			properties.store(output, null);
		} catch (IOException e) {
			
		} finally {
			if(output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void loadData() {
		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("accountcreator.properties");
			
			properties.load(input);
			
			if(Boolean.parseBoolean(properties.getProperty("custom_users"))) {
				listUsernames.setSelected(true);
				userFilePath.setForeground(Color.BLACK);
				userFilePath.setText(properties.getProperty("user_file_path"));
			}
			
			if(Boolean.parseBoolean(properties.getProperty("custom_passes"))) {
				listPasses.setSelected(true);
				passFilePath.setForeground(Color.BLACK);
				passFilePath.setText(properties.getProperty("pass_file_path"));
			}
			
			if(Boolean.parseBoolean(properties.getProperty("one_pass"))) {
				singularPassword.setSelected(true);
				thePass.setForeground(Color.BLACK);
				thePass.setText(properties.getProperty("singular_password"));
			}
			
			if(Boolean.parseBoolean(properties.getProperty("log_accounts"))) {
				logAccounts.setSelected(true);
				accountLogPath.setForeground(Color.BLACK);
				accountLogPath.setText(properties.getProperty("account_log_path"));
			}
			
			accountCount.setText(properties.getProperty("accounts_to_make"));
			captchaKey.setText(properties.getProperty("captcha_key"));
			
			if(Boolean.parseBoolean(properties.getProperty("use_proxies"))) {
				enableProxies.setSelected(true);
				proxyList.setForeground(Color.BLACK);
				proxyList.setText(properties.getProperty("proxy_list"));
			}
			
			linearRotation.setSelected(Boolean.parseBoolean(properties.getProperty("use_proxies_linearly")));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}
}

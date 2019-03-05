package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;

import utils.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CreationHandler extends JFrame {

	private static final long serialVersionUID = 5604068803577379206L;
	private JPanel contentPane;
	
	private final AccountCreator accountCreator;
	private int accountsCreated = 0;
	
	private final int createCount;
	
	private JTextArea logger, accountsMade;
	private boolean logAccounts, useProxiesLinearly, useProxies;
	
	private final String accountLogPath;
	
	private int currentProxy = 0;
	private List<UserProxy> proxies = new ArrayList<>();
	
	private final StringGenerator userMaker, passMaker;

	/**
	 * Create the frame.
	 */
	public CreationHandler(CaptchaService service, int createCount, String key, boolean logAccounts, String accountLogPath, boolean useProxies, 
								boolean useProxiesLinearly, List<UserProxy> proxies, StringGenerator userMaker, StringGenerator passMaker) {
		
		this.accountCreator = new AccountCreator(key, service);

		this.createCount = createCount;
		this.logAccounts = logAccounts;
		this.accountLogPath = accountLogPath;
		
		this.useProxiesLinearly = useProxiesLinearly;
		this.useProxies = useProxies;
		this.proxies = proxies;
		
		this.userMaker = userMaker;
		this.passMaker = passMaker;
		
		setBounds(100, 100, 550, 400);
		setTitle(Strings.getScriptName());
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		logger = new JTextArea();
		logger.setEditable(false);
		logger.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Logger", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		logger.setBackground(contentPane.getBackground());
		
		JScrollPane loggerScroll = new JScrollPane(logger);
		loggerScroll.setBounds(6, 6, 540, 175);
		contentPane.add(loggerScroll);
		
		accountsMade = new JTextArea();
		accountsMade.setEditable(false);
		accountsMade.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Accounts Created", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		accountsMade.setBackground(contentPane.getBackground());
		
		JScrollPane accsCreatedScroll = new JScrollPane(accountsMade);
		accsCreatedScroll.setBounds(6, 195, 540, 175);
		contentPane.add(accsCreatedScroll);
		
		
		CreationHandler frame = this;
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
	}
	
	private boolean shouldCreateAccounts() {
		return accountsCreated < createCount;
	}
	
	private String generateUsername() {
		return userMaker.generateString();
	}
	
	private String generatePassword() {
		return passMaker.generateString();
	}
	
	private UserProxy getProxy() {
		if(useProxies && proxies != null && proxies.size() > 0) {
			if(useProxiesLinearly) {
				if(currentProxy == proxies.size())
					currentProxy = 0;
				return proxies.get(currentProxy++);
			}
			return proxies.get(Random.random(proxies.size()));
		}
		return null;
	}
	
	public static void main(String[] args) throws InterruptedException {
		GUI gui = new GUI();
		while(!gui.getStarted())
			Thread.sleep(1000);
		
		CreationHandler handler = gui.getHandler();
		handler.setVisible(true);
		
		int failCount = 0;
		
		Logger.setup(handler.logger, handler.accountsMade, handler.logAccounts, handler.accountLogPath);
		while(handler.shouldCreateAccounts()) {
			if(handler.accountCreator.createAccount(handler.generateUsername(), handler.generatePassword(), handler.getProxy()))
				handler.accountsCreated++;
			else
				failCount++;
			
			
			int total = failCount + handler.accountsCreated;
			
			Logger.addMessage("Success Count: %d. Fail Count: %d.", handler.accountsCreated, failCount);
			Logger.addMessage("Rate of success: %f", 100.0*handler.accountsCreated/total);
		}
		Logger.addMessage("Completed all accounts!");
	}
}

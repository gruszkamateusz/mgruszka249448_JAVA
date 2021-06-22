package billboardpack;

import bilboards.IBillboard;
import bilboards.IManager;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Advert {
	public String advertText;
	public Duration displayPeriod;
	public int orderId;
}

public class Billboard extends JFrame implements IBillboard {
	private static final String defaultMessage = "Place for advert";
	private IBillboard billboardInterface;
	private Registry registry;
	private String name;
	private String managerName;
	private int bilPort;
	private String registryHost;
	private int regPort;	
	private int billboardId;
	
	private int capacity;
	private Map<Integer, Advert> adverts;
	private List<Advert> displayQueue;
	private Duration displayInterval;
	private Thread displayThread;
	private JTextArea textAreaAdvert;
	private boolean end;

	public Billboard() {
		name = "Billboard";
		managerName = "Manager-server";
		bilPort = 4000;
		registryHost = "127.0.0.1";
		regPort = 1500;
		displayThread = null;
		setTitle(name);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(300, 300);
		setSize(300, 300);
		setPreferredSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(300, 300));
		capacity = 10;
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPane);
		textAreaAdvert = new JTextArea();
		scrollPane.setViewportView(textAreaAdvert);
		textAreaAdvert.setFont(new Font("", Font.BOLD | Font.ITALIC, 24));

		adverts = new HashMap<Integer, Advert>();
		displayInterval = Duration.ofSeconds(3);
		displayQueue = new ArrayList<Advert>();

		end = false;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					stop();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				unregist();
			}
		});

		editSettings();
	}

	public static void main(String[] args) {
		Billboard billboard = new Billboard();
		billboard.setVisible(true);
		System.setProperty("java.security.policy", "./java.policy");
        System.setSecurityManager(new SecurityManager());
	}

	@Override
	public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
		System.out.println("New advert: " + advertText);
		if (adverts.size() < capacity) {
			Advert newAdvert = new Advert();
			newAdvert.advertText = advertText;
			newAdvert.displayPeriod = displayPeriod;
			newAdvert.orderId = orderId;
			synchronized (adverts) {
				adverts.put(Integer.valueOf(orderId), newAdvert);
				displayQueue.add(newAdvert);
			}
			System.out.println("Done");
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAdvertisement(int orderId) throws RemoteException {
		boolean r = false;
		synchronized (adverts) {
			Advert ad = adverts.remove(Integer.valueOf(orderId));
			if (ad != null) {
				displayQueue.remove(ad);
				r =  true;
			}
		}
		return r;
	}

	@Override
	public int[] getCapacity() throws RemoteException {
		return new int[] { capacity, capacity-adverts.size() };
	}

	@Override
	public void setDisplayInterval(Duration displayInterval) throws RemoteException {
		this.displayInterval = displayInterval;
	}

	@Override
	public boolean start() throws RemoteException {
		if (displayThread == null) {
			end = false;
			
			displayThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					while (end != true) {
						Duration duration = Duration.ofMillis(100);
						
						if(adverts.size() > 0) {
							
							synchronized (adverts) {
								Advert ad = displayQueue.get(0);
								textAreaAdvert.setText(ad.advertText);
								displayQueue.remove(0);
								
								if (ad.displayPeriod.compareTo(displayInterval) <= 0) {
									duration = ad.displayPeriod;
									adverts.remove(ad.orderId);
								} else {
									displayQueue.add(ad);
									duration = displayInterval;
									ad.displayPeriod = ad.displayPeriod.minus(displayInterval);
								}
							}
						} else {
							textAreaAdvert.setText(defaultMessage);
						}
						
						try {
							Thread.sleep(duration.toMillis());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			displayThread.start();
			return true;
		}
		return false;
	}

	@Override
	public boolean stop() throws RemoteException {
		end = true;
		displayThread = null;
		return true;
	}

	public void editSettings() {
		if(billboardInterface != null) {
			unregist();
		}
		
		JTextField regHostField = new JTextField(5);
		JTextField regPortField = new JTextField(5);
		JTextField bilPortField = new JTextField(5);
		JTextField bilNameField = new JTextField(5);
		JTextField manNameField = new JTextField(5);
		JTextField dispIntervalField = new JTextField(5);
		JTextField dispBuffSizeField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setPreferredSize(new Dimension(200, 200));
		
		myPanel.add(new JLabel("RMI host:"));
		myPanel.add(regHostField);
		regHostField.setText(String.valueOf(registryHost));
		
		myPanel.add(new JLabel("RMI Registry port:"));
		myPanel.add(regPortField);
		regPortField.setText(String.valueOf(regPort));
		
		myPanel.add(new JLabel("Billboard port:"));
		myPanel.add(bilPortField);
		bilPortField.setText(String.valueOf(bilPort));
		
		myPanel.add(new JLabel("Billboard name:"));
		myPanel.add(bilNameField);
		bilNameField.setText(name);
		
		myPanel.add(new JLabel("Manager name:"));
		myPanel.add(manNameField);
		manNameField.setText(managerName);
		
		myPanel.add(new JLabel("Display interval [s]:"));
		myPanel.add(dispIntervalField);
		dispIntervalField.setText(Long.toString(displayInterval.toSeconds()));
		
		myPanel.add(new JLabel("Display buffer size:"));
		myPanel.add(dispBuffSizeField);
		dispBuffSizeField.setText(Integer.toString(capacity));

		JOptionPane.showConfirmDialog(null, myPanel, "Billboard settings",
				JOptionPane.PLAIN_MESSAGE);
		System.out.println("RMI Registry port:: " + regPortField.getText());
		System.out.println("Client port: " + bilPortField.getText());
		System.out.println("Client name: " + bilNameField.getText());
		System.out.println("Manager name: " + manNameField.getText());
		System.out.println("Display interval [s]: " + dispIntervalField.getText());
		System.out.println("Display buffer size: " + dispIntervalField.getText());
		
		registryHost = regHostField.getText();
		regPort = Integer.parseInt(regPortField.getText());
		bilPort = Integer.parseInt(bilPortField.getText());
		name = bilNameField.getText();
		managerName = manNameField.getText();
		displayInterval = Duration.ofSeconds(Long.parseLong(dispIntervalField.getText()));
		capacity = Integer.parseInt(dispBuffSizeField.getText());
		
		regist();
	}
	public boolean regist() {
		try {
			registry = LocateRegistry.getRegistry(registryHost, regPort);
			billboardInterface = (IBillboard) UnicastRemoteObject.exportObject(this, bilPort);
			IManager mi = (IManager) registry.lookup(managerName);
			billboardId = mi.bindBillboard(billboardInterface);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean unregist() {
		try {
			IManager mi = (IManager) registry.lookup(managerName);
			mi.unbindBillboard(billboardId);
			UnicastRemoteObject.unexportObject(billboardInterface, true);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

package clientpack;

import bilboards.IClient;
import bilboards.IManager;
import bilboards.Order;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Client extends JFrame implements IClient {
	private String name;
	private String managerName;
	private String registryHost;
	private int registryPort;
	private int clientPort;
    private IClient clientInterface;
    private Registry registry;
    private JTextField textFieldAdDuration;
    
    private JTable tableAds;
    private DefaultTableModel modelAds;
    private Map<Integer, Order> ads;
    private int orderId;

    public Client() {
    	clientPort = 3000;
    	registryHost = "127.0.0.1";
    	registryPort = 1500;
    	name = "Client";
    	managerName = "Manager-server";
    	setTitle(name);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(300, 300);
        setSize(550, 350);
        setPreferredSize(new Dimension(500, 350));
        setMinimumSize(new Dimension(500, 300));
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        
        JScrollPane scrollPaneAds = new JScrollPane();
        springLayout.putConstraint(SpringLayout.WEST, scrollPaneAds, 0, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneAds, -100, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, scrollPaneAds, 0, SpringLayout.EAST, getContentPane());
        getContentPane().add(scrollPaneAds);
        
        JLabel lblActiveAds = new JLabel("Active adverts:");
        springLayout.putConstraint(SpringLayout.NORTH, scrollPaneAds, 10, SpringLayout.SOUTH, lblActiveAds);
        springLayout.putConstraint(SpringLayout.NORTH, lblActiveAds, 10, SpringLayout.NORTH, getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, lblActiveAds, 10, SpringLayout.WEST, getContentPane());
        getContentPane().add(lblActiveAds);
        
        JLabel lblAdContent = new JLabel("Advert content:");
        springLayout.putConstraint(SpringLayout.NORTH, lblAdContent, 10, SpringLayout.SOUTH, scrollPaneAds);
        springLayout.putConstraint(SpringLayout.WEST, lblAdContent, 10, SpringLayout.WEST, getContentPane());
        getContentPane().add(lblAdContent);
        
        JLabel lblAdDuration = new JLabel("Advert duration:(seconds)");
        springLayout.putConstraint(SpringLayout.NORTH, lblAdDuration, 0, SpringLayout.NORTH, lblAdContent);
        springLayout.putConstraint(SpringLayout.WEST, lblAdDuration, 73, SpringLayout.EAST, lblAdContent);
        getContentPane().add(lblAdDuration);
        
        textFieldAdDuration = new JTextField();
        springLayout.putConstraint(SpringLayout.NORTH, textFieldAdDuration, 6, SpringLayout.SOUTH, lblAdDuration);
        springLayout.putConstraint(SpringLayout.WEST, textFieldAdDuration, 0, SpringLayout.WEST, lblAdDuration);
        getContentPane().add(textFieldAdDuration);
        textFieldAdDuration.setColumns(10);
        
        JTextArea textAreaAdContent = new JTextArea();
        springLayout.putConstraint(SpringLayout.NORTH, textAreaAdContent, 5, SpringLayout.SOUTH, lblAdContent);
        springLayout.putConstraint(SpringLayout.WEST, textAreaAdContent, 10, SpringLayout.WEST, getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, textAreaAdContent, -10, SpringLayout.SOUTH, getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, textAreaAdContent, 150, SpringLayout.WEST, getContentPane());
        getContentPane().add(textAreaAdContent);
        
        JButton btnOrderNewAd = new JButton("Order new advert");
        springLayout.putConstraint(SpringLayout.NORTH, btnOrderNewAd, 6, SpringLayout.SOUTH, textFieldAdDuration);
        springLayout.putConstraint(SpringLayout.WEST, btnOrderNewAd, 0, SpringLayout.WEST, lblAdDuration);
        getContentPane().add(btnOrderNewAd);
        btnOrderNewAd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Order order = new Order();
				order.advertText = textAreaAdContent.getText();
				order.client = clientInterface;
				try {
					order.displayPeriod = Duration.ofSeconds(Integer.parseInt(textFieldAdDuration.getText()));
				} catch (Exception e2 ) { 
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Duration must be given in integer number.", "Order place error", JOptionPane.ERROR_MESSAGE);
				}

				IManager mi;
				try {
					mi = (IManager) registry.lookup(managerName);
					if(mi.placeOrder(order)) {
						Object[] row = new Object[3];
		        		row[0] = Integer.valueOf(orderId);
		        		row[1] = order.advertText;
		        		row[2] = order.displayPeriod;
		        		modelAds.addRow(row);
		        		ads.put(orderId, order);
		        		
					}else {
						JOptionPane.showMessageDialog(null, "Could not place order.", "Order place error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (RemoteException | NotBoundException e1) {
					e1.printStackTrace();
				}
			}
		});
        
        JButton btnCancelAd = new JButton("Cancel selected advert");
        springLayout.putConstraint(SpringLayout.NORTH, btnCancelAd, 0, SpringLayout.NORTH, lblAdContent);
        springLayout.putConstraint(SpringLayout.EAST, btnCancelAd, -10, SpringLayout.EAST, getContentPane());
        getContentPane().add(btnCancelAd);
        btnCancelAd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowIdx = tableAds.getSelectedRow();
				IManager mi;
				try {
					mi = (IManager) registry.lookup(managerName);
					if(mi.withdrawOrder((Integer)modelAds.getValueAt(rowIdx, 0)) == true) {
						modelAds.removeRow(rowIdx);
						ads.remove(rowIdx);
					} else {
						JOptionPane.showMessageDialog(null, "Could not cancel order.", "Order cancel error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (RemoteException | NotBoundException e1) {
					e1.printStackTrace();
				}
			}
		});
        
        tableAds = new JTable();
        scrollPaneAds.setViewportView(tableAds);
        Object[] columnsAds = {"Id","Ad content","Duration"};
        modelAds = new DefaultTableModel();
        modelAds.setColumnIdentifiers(columnsAds);
        tableAds.setModel(modelAds);
        tableAds.setBackground(Color.white);
        tableAds.setForeground(Color.black);
        tableAds.setRowHeight(20);
        tableAds.setFont(new Font("", 1, 14));
        
        JButton btnClearHistory = new JButton("Clear advert history");
        btnClearHistory.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i < modelAds.getRowCount(); ++i) {
        			modelAds.removeRow(0);        			
        		}
        		ads.clear();
        	}
        });
        springLayout.putConstraint(SpringLayout.NORTH, btnClearHistory, 6, SpringLayout.SOUTH, btnCancelAd);
        springLayout.putConstraint(SpringLayout.WEST, btnClearHistory, 0, SpringLayout.WEST, btnCancelAd);
        springLayout.putConstraint(SpringLayout.EAST, btnClearHistory, 0, SpringLayout.EAST, btnCancelAd);
        getContentPane().add(btnClearHistory);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	unregist();
            }
        });
        
        ads = new HashMap<Integer, Order>();
        editSettings();
    }

    public static void main(String[] args) {
    	Client client = new Client();
    	client.setVisible(true);
    }

    @Override
    public void setOrderId(int orderId) throws RemoteException {
        this.orderId = orderId;
    }

	public void editSettings() {
		if(clientInterface != null) {
			unregist();
		}
		
		JTextField regHostField = new JTextField(5);
		JTextField regPortField = new JTextField(5);
		JTextField clPortField = new JTextField(5);
		JTextField clNameField = new JTextField(5);
		JTextField manNameField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setPreferredSize(new Dimension(180, 200));
		
		myPanel.add(new JLabel("RMI host:"));
		myPanel.add(regHostField);
		regHostField.setText(String.valueOf(registryHost));
		
		myPanel.add(new JLabel("RMI port:"));
		myPanel.add(regPortField);
		regPortField.setText(String.valueOf(registryPort));
		
		myPanel.add(new JLabel("Client port:"));
		myPanel.add(clPortField);
		clPortField.setText(String.valueOf(clientPort));
		
		myPanel.add(new JLabel("Client name:"));
		myPanel.add(clNameField);
		clNameField.setText(name);
		
		myPanel.add(new JLabel("Manager name:"));
		myPanel.add(manNameField);
		manNameField.setText(managerName);

		JOptionPane.showConfirmDialog(null, myPanel, "Client settings",
				JOptionPane.PLAIN_MESSAGE);
		
		registryHost = regHostField.getText();
		registryPort = Integer.parseInt(regPortField.getText());
		clientPort = Integer.parseInt(clPortField.getText());
		name = clNameField.getText();
		managerName = manNameField.getText();
		
		regist();
	}
	
    public boolean regist() {
        try {
            registry = LocateRegistry.getRegistry(registryHost, registryPort);
            clientInterface = (IClient) UnicastRemoteObject.exportObject(this, clientPort);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean unregist() {
        try {
        		UnicastRemoteObject.unexportObject(clientInterface, true);
        		clientInterface = null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

package queuesyspack;

import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class ManagerApp extends ManagerFrame implements NotificationListener {
	private static final long serialVersionUID = -6806001372110992484L;
	private JTextArea taNotifications;
	private JTextField tfAgentPort;
	private int agentPort;
	private MBeanServerConnection mbs;
	private JMXConnector connector;
	private AgentMXBean agentProxy;
	private JButton btnSyncList;
	private JButton btnConnect;

	public ManagerApp(DefaultListModel<TicketCategory> listModel) {
		super(listModel);
		SpringLayout springLayout = (SpringLayout) getContentPane().getLayout();

		setLocation(300, 300);
		setSize(300, 440);
		setPreferredSize(new Dimension(300, 440));
		setMinimumSize(new Dimension(300, 440));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 125, SpringLayout.WEST, getContentPane());
		getContentPane().add(scrollPane);

		JScrollPane scrollPaneNotif = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPaneNotif, 300, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPaneNotif, 6, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneNotif, -6, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPaneNotif, -6, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPaneNotif);

		taNotifications = new JTextArea();
		scrollPaneNotif.setViewportView(taNotifications);

		JLabel lblLogs = new JLabel("Notifications:");
		springLayout.putConstraint(SpringLayout.WEST, lblLogs, 0, SpringLayout.WEST, scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, lblLogs, -6, SpringLayout.NORTH, scrollPaneNotif);
		getContentPane().add(lblLogs);

		JLabel lblAgentPort = new JLabel("Agent port:");
		springLayout.putConstraint(SpringLayout.WEST, lblAgentPort, 0, SpringLayout.WEST, scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, lblAgentPort, -66, SpringLayout.NORTH, lblLogs);
		getContentPane().add(lblAgentPort);

		tfAgentPort = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, tfAgentPort, 6, SpringLayout.SOUTH, lblAgentPort);
		springLayout.putConstraint(SpringLayout.WEST, tfAgentPort, 0, SpringLayout.WEST, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, tfAgentPort, 0, SpringLayout.EAST, scrollPane);
		getContentPane().add(tfAgentPort);
		tfAgentPort.setColumns(10);

		btnConnect = new JButton("Connect");
		springLayout.putConstraint(SpringLayout.NORTH, btnConnect, 6, SpringLayout.SOUTH, tfAgentPort);
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, 0, SpringLayout.EAST, scrollPane);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnConnect.getText() == "Connect") {
					connect();
				} else {
					disconnect();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, btnConnect, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(btnConnect);

		agentPort = 8008;
		tfAgentPort.setText(Integer.toString(agentPort));

		btnSyncList = new JButton("Sync list");
		springLayout.putConstraint(SpringLayout.WEST, btnSyncList, -135, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnSyncList, -6, SpringLayout.SOUTH, lblLogs);
		btnSyncList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateList();
			}
		});
		springLayout.putConstraint(SpringLayout.EAST, btnSyncList, -21, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnSyncList);
	}

	@Override
	public void handleNotification(Notification notification, Object handback) {
		updateList();
		taNotifications.append(notification.getMessage() + System.getProperty("line.separator"));
	}
	
	public void connect() {
		agentPort = Integer.parseInt(tfAgentPort.getText());

		JMXServiceURL target;
		try {
			target = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + agentPort + "/jmxrmi");
			connector = JMXConnectorFactory.connect(target);
			mbs = connector.getMBeanServerConnection();
			ObjectName name = new ObjectName("agentmodule:name=Agent");
			agentProxy = JMX.newMXBeanProxy(mbs, name, AgentMXBean.class);
			mbs.addNotificationListener(name, this, null, null);
			updateList();
		} catch (IOException | MalformedObjectNameException | InstanceNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
		tfAgentPort.setEditable(false);
		btnConnect.setText("Disconnect");
		btnSyncList.setEnabled(true);
	}
	
	public void disconnect() {
		tfAgentPort.setEditable(true);
		try {
			mbs.removeNotificationListener(new ObjectName("agentmodule:name=Agent"), this);
			connector.close();
			connector = null;
			mbs = null;
			agentProxy = null;
			connector = null;
		} catch (IOException | InstanceNotFoundException | ListenerNotFoundException | MalformedObjectNameException e1) {
			e1.printStackTrace();
		}

		btnConnect.setText("Connect");
		btnSyncList.setEnabled(false);
	}
	
	public void updateNextSymbol() {
		TicketCategory.nextSymbol = agentProxy.getNextCategorySymbol();
	}
	
	public void updateList() {
		agentProxy.hello();
		updateNextSymbol();
		DefaultListModel<TicketCategory> listModel = new DefaultListModel<TicketCategory>();
		TicketCategory[] catList = agentProxy.getTicketCategoryList();
		for(TicketCategory c : catList) {
			listModel.addElement(c);
		}
		setTicketCategoryList(listModel);
	}
	
	@Override
	public TicketCategory addNew() {
		TicketCategory cat = super.addNew();
		agentProxy.addTicketCategory(cat);
		agentProxy.setNextCategorySymbol(TicketCategory.nextSymbol);
		return cat;
	}

	@Override
	public TicketCategory removeSelected() {
		TicketCategory cat = super.removeSelected();
		agentProxy.removeTicketCategory(cat);
		return cat;
	}

	@Override
	public TicketCategory editSelected() {
		TicketCategory cat = super.editSelected();
		agentProxy.addTicketCategory(cat);
		return cat;
	}

	public static void main(String[] args) {
		new ManagerApp(null).setVisible(true);
	}
}

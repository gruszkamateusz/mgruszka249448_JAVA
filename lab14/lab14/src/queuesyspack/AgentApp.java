package queuesyspack;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextArea;

/*
Run AgentApp.jar with:
java.exe -Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.port=8008 \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false -jar AgentApp.jar
*/
public class AgentApp extends NotificationBroadcasterSupport implements AgentMXBean, ManagerListener {
	private JList<TicketCategory> tickCatList;
	private DefaultListModel<TicketCategory> tickCatListModel;
	private JTextField tfCurrentClient;
	private JTextArea taQueue;
	private StringBuilder queueView;
	private List<Ticket> ticketQueue;
	private Thread simThread;
	private boolean simRunning;
	private Thread serviceThread;
	private boolean serviceRunning;
	private long sequenceNumber;
	private JFrame frame;

	public AgentApp() {
		frame = new JFrame();
		frame.setTitle("Agent");
		frame.setLocation(300, 300);
		frame.setSize(340, 300);
		frame.setPreferredSize(new Dimension(340, 300));
		frame.setMinimumSize(new Dimension(340, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		JLabel lblNewLabel = new JLabel("Ticket Machine");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, frame.getContentPane());
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Information Board");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 0, SpringLayout.NORTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_1, 102, SpringLayout.EAST, lblNewLabel);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(lblNewLabel_1);

		JScrollPane scrollPaneTicketCategory = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPaneTicketCategory, 6, SpringLayout.SOUTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, scrollPaneTicketCategory, 10, SpringLayout.WEST,
				frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneTicketCategory, 181, SpringLayout.SOUTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.EAST, scrollPaneTicketCategory, 183, SpringLayout.WEST,
				frame.getContentPane());
		frame.getContentPane().add(scrollPaneTicketCategory);
		tickCatListModel = new DefaultListModel<TicketCategory>();
		tickCatList = new JList<TicketCategory>(tickCatListModel);
		scrollPaneTicketCategory.setViewportView(tickCatList);

		JButton btnGetTicket = new JButton("Get ticket");
		springLayout.putConstraint(SpringLayout.NORTH, btnGetTicket, 6, SpringLayout.SOUTH, scrollPaneTicketCategory);
		springLayout.putConstraint(SpringLayout.EAST, btnGetTicket, 0, SpringLayout.EAST, scrollPaneTicketCategory);
		btnGetTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (ticketQueue) {
					TicketCategory category = tickCatList.getSelectedValue();
					addTicketIntoQueue(category.createTicket());
				}
			}
		});
		frame.getContentPane().add(btnGetTicket);

		tfCurrentClient = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, tfCurrentClient, 47, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, tfCurrentClient, 0, SpringLayout.WEST, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.EAST, tfCurrentClient, 0, SpringLayout.EAST, lblNewLabel_1);
		frame.getContentPane().add(tfCurrentClient);
		tfCurrentClient.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Current client:");
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_2, 0, SpringLayout.WEST, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel_2, 0, SpringLayout.NORTH, tfCurrentClient);
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel_2, 0, SpringLayout.EAST, lblNewLabel_1);
		frame.getContentPane().add(lblNewLabel_2);

		JScrollPane scrollPaneQueue = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, scrollPaneQueue, 18, SpringLayout.EAST, scrollPaneTicketCategory);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneQueue, -24, SpringLayout.SOUTH,
				frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPaneQueue, 129, SpringLayout.EAST,
				scrollPaneTicketCategory);
		frame.getContentPane().add(scrollPaneQueue);
		taQueue = new JTextArea();
		scrollPaneQueue.setViewportView(taQueue);
		taQueue.setEditable(false);

		JLabel lblQueue = new JLabel("Queue:");
		springLayout.putConstraint(SpringLayout.NORTH, scrollPaneQueue, 6, SpringLayout.SOUTH, lblQueue);
		springLayout.putConstraint(SpringLayout.NORTH, lblQueue, 6, SpringLayout.SOUTH, tfCurrentClient);
		springLayout.putConstraint(SpringLayout.WEST, lblQueue, 0, SpringLayout.WEST, lblNewLabel_1);
		springLayout.putConstraint(SpringLayout.EAST, lblQueue, 0, SpringLayout.EAST, lblNewLabel_1);
		frame.getContentPane().add(lblQueue);

		JButton btnNewButton = new JButton("Manage");
		btnNewButton.addActionListener((event) -> {
			new Thread(() -> {
				ManagerFrame manager = new ManagerFrame(tickCatListModel);
				manager.setVisible(true);
				manager.addManagerListener(this);

				while (manager.isVisible()) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				manager.dispose();
				manager.removeManagerListener(this);
			}).start();
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 6, SpringLayout.SOUTH, scrollPaneTicketCategory);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 0, SpringLayout.WEST, lblNewLabel);
		frame.getContentPane().add(btnNewButton);

		ticketQueue = new ArrayList<Ticket>();

		tickCatListModel.addElement(new TicketCategory("Passport", 1));
		tickCatListModel.addElement(new TicketCategory("ID card", 2));
		tickCatListModel.addElement(new TicketCategory("Driving license", 3));

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopSimulation();
				stopService();
			}
		});

		sequenceNumber = 0;
		queueView = new StringBuilder("");
		frame.setVisible(true);
		registAgent();
		startSimulation();
		startSerivceThread();
	}

	public void addTicketIntoQueue(Ticket ticket) {
		String lnsep = System.getProperty("line.separator");
		int p = ticket.category.priority;
		int idx = 0;
		StringBuilder newQueue = new StringBuilder("");		
		
		for (int i = ticketQueue.size() - 1; i >= 0; --i) 
			if (ticketQueue.get(i).category.priority >= p) {
				idx = i + 1;
				break;
			}
		
		if(idx == 0) {
			newQueue.append(ticket.toString()).append(lnsep).append(queueView.toString());
		} else {
			int idp;
			if(idx == ticketQueue.size())
				idp = idx - 1;
			else { //give way to one lower priority ticket to avoid endless/long waiting
				idp = idx;
				++idx;
			}
			
			String reg = "\\s*" + ticketQueue.get(idp).toString() + "\\s*";
			String[] parts = queueView.toString().split(reg, 2);
			
			if(parts[0].matches("^\\s*$") != true) {
				newQueue.append(parts[0]).append(lnsep);
			}
			
			newQueue.append(ticketQueue.get(idp).toString()).append(lnsep)
					.append(ticket.toString())
					.append(lnsep);
			
			if(parts.length == 2 && parts[1].matches("^\\s*$") != true) {
				newQueue.append(parts[1]);
			}
		}
			
		queueView = newQueue;
		ticketQueue.add(idx, ticket);
		taQueue.setText(newQueue.toString());
	}

	public void startSimulation() {
		if (!simRunning) {
			simRunning = true;
			simThread = new Thread() {
				public void run() {
					try {
						while (simRunning == true && !Thread.interrupted()) {
							int idx = ThreadLocalRandom.current().nextInt(0, tickCatListModel.getSize());
							TicketCategory category = tickCatListModel.get(idx);
							synchronized (ticketQueue) {
								addTicketIntoQueue(category.createTicket());
							}

							Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
						}
						simRunning = false;
					} catch (InterruptedException e) {
					}
					System.out.println("End of Service");
				}
			};

			simThread.start();
		}
	}

	public void startSerivceThread() {
		if (!serviceRunning) {
			serviceRunning = true;
			serviceThread = new Thread(() -> {
				String reg = "^\\s*.+\\s*";
				while (serviceRunning) {
					synchronized (ticketQueue) {
						queueView = new StringBuilder(queueView.toString().replaceFirst(reg, ""));
						taQueue.setText(queueView.toString());

						if (ticketQueue.size() > 0) {
							tfCurrentClient.setText(ticketQueue.get(0).toString());
							ticketQueue.remove(0);
						} else
							tfCurrentClient.setText("");
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			serviceThread.start();
		}
	}

	public void stopSimulation() {
		try {
			simThread.interrupt();
			simRunning = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopService() {
		try {
			serviceThread.interrupt();
			serviceRunning = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registAgent() {
		try {
			ObjectName objName = new ObjectName("agentmodule:name=Agent");
			ManagementFactory.getPlatformMBeanServer().registerMBean(this, objName);
			int port = Integer.parseInt(System.getProperty("com.sun.management.jmxremote.port"));
			frame.setTitle("Agent, port: " + port);
		} catch (InstanceAlreadyExistsException | NotCompliantMBeanException | MalformedObjectNameException
				| MBeanException e) {
			e.printStackTrace();
		} catch (NumberFormatException  e) {
			JOptionPane.showMessageDialog(null, "Run AgentApp with -Dcom.sun.management.jmxremote\r\n" + 
					"-Dcom.sun.management.jmxremote.port=8008\r\n" + 
					"-Dcom.sun.management.jmxremote.authenticate=false\r\n" + 
					"-Dcom.sun.management.jmxremote.ssl=false");
		}
	}

	@Override
	public void hello() {
		System.out.println("Hello from Agent of Queue System");
	}

	@Override
	public void addTicketCategory(TicketCategory category) {
		tickCatListModel.addElement(category);
	}

	@Override
	public void removeTicketCategory(TicketCategory category) {
		for (int i = 0; i < tickCatListModel.getSize(); ++i) {
			if (tickCatListModel.get(i).symbol == category.symbol) {
				tickCatListModel.remove(i);
				return;
			}
		}
	}

	@Override
	public void editTicketCategory(TicketCategory category) {
		for (int i = 0; i < tickCatListModel.getSize(); ++i) {
			if (tickCatListModel.get(i).symbol == category.symbol) {
				tickCatListModel.add(i, category);
				;
				return;
			}
		}
	}

	@Override
	public TicketCategory[] getTicketCategoryList() {
		TicketCategory[] catTab = new TicketCategory[tickCatListModel.getSize()];
		for (int i = 0; i < tickCatListModel.getSize(); ++i) {
			catTab[i] = tickCatListModel.get(i);
		}
		return catTab;
	}

	@Override
	public char getNextCategorySymbol() {
		return TicketCategory.nextSymbol;
	}

	@Override
	public void setNextCategorySymbol(char symbol) {
		TicketCategory.nextSymbol = symbol;
	}

	public void notifyCategoryChange(String msg) {
		Notification notification = new Notification("queuesyspack.AgentFrame", this, ++sequenceNumber, msg);
		sendNotification(notification);
	}

	public static void main(String[] args) {
		new AgentApp();
	}

	@Override
	public void actionPerformed(String msg) {
		notifyCategoryChange(msg);
	}
}

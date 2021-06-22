package managerpack;

import bilboards.IBillboard;
import bilboards.IManager;
import bilboards.Order;
import billboardpack.Billboard;

import javax.swing.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

class Advert {
	public Order order;
	public int orderId;
	public int billboardId;
}

public class Manager extends JFrame implements IManager {
	private boolean end;
	private String name;
	private int registryPort;
	private int managerPort;
	private IManager managerInterface;
	private Registry registry;

	private JTable tableBllbrds;
	private DefaultTableModel modelBllbrds;
	private Map<Integer, IBillboard> billboards;
	private int billboardsIds;

	private Map<Integer, Advert> orders;
	private int ordersIds;

	public Manager() {
		this.name = "Manager-server";
		managerPort = 2000;
		registryPort = 1500;
		setTitle(name);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocation(300, 300);
		setSize(350, 400);
		setPreferredSize(new Dimension(350, 400));
		setMinimumSize(new Dimension(300, 300));

		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		tableBllbrds = new JTable();
		JScrollPane scrollPaneBllbrds = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPaneBllbrds, 20, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPaneBllbrds, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneBllbrds, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPaneBllbrds, 0, SpringLayout.EAST, getContentPane());
		scrollPaneBllbrds.setViewportView(tableBllbrds);
		getContentPane().add(scrollPaneBllbrds);

		JLabel lblBllbrdsTab = new JLabel("Billboards:");
		springLayout.putConstraint(SpringLayout.NORTH, lblBllbrdsTab, 5, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblBllbrdsTab, 5, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblBllbrdsTab);
		Object[] columnsBllbrds = { "Id", "Capacity", "Free spaces" };
		modelBllbrds = new DefaultTableModel();
		modelBllbrds.setColumnIdentifiers(columnsBllbrds);
		tableBllbrds.setModel(modelBllbrds);
		tableBllbrds.setBackground(Color.white);
		tableBllbrds.setForeground(Color.black);
		tableBllbrds.setRowHeight(20);
		tableBllbrds.setFont(new Font("", 1, 14));

		billboardsIds = 0;
		billboards = new HashMap<Integer, IBillboard>();

		ordersIds = 0;
		orders = new HashMap<Integer, Advert>();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int id;
				end = true;
				synchronized (billboards) {
					for (int i = 0; i < modelBllbrds.getRowCount(); ++i) {
						id = (Integer) modelBllbrds.getValueAt(i, 0);
						billboards.remove(id);
						modelBllbrds.removeRow(0);
					}
				}
				unregist();
			}
		});

		end = false;

		new Thread(new Runnable() {
			@Override
			public void run() {
				int id, capacity;
				while (end != true) {
					synchronized (billboards) {
						for (int i = 0; i < modelBllbrds.getRowCount(); ++i) {
							id = (Integer) modelBllbrds.getValueAt(i, 0);
							try {
								capacity = billboards.get(id).getCapacity()[1];
								modelBllbrds.setValueAt(Integer.valueOf(capacity), i, 2);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		editSettings();
	}

	public static void main(String[] args) {
		Manager manager = new Manager();
		manager.setVisible(true);
	}

	@Override
	public int bindBillboard(IBillboard billboard) throws RemoteException {
		int id = -1;
		synchronized (billboards) {
			billboards.put(billboardsIds, billboard);
			modelBllbrds.addRow(new Object[] { Integer.valueOf(billboardsIds), billboard.getCapacity()[0],
					billboard.getCapacity()[1] });
			id = billboardsIds++;
		}
		billboard.start();
		return id;
	}

	@Override
	public boolean unbindBillboard(int billboardId) throws RemoteException {
		boolean r = false;
		synchronized (billboards) {
			for (int i = 0; i < modelBllbrds.getRowCount(); ++i) {
				if ((Integer) modelBllbrds.getValueAt(i, 0) == billboardId) {
					billboards.remove(billboardId).stop();
					modelBllbrds.removeRow(i);
					r = true;
				}
			}
		}
		return r;
	}

	@Override
	public boolean placeOrder(Order order) throws RemoteException {
		int id = -1;
		if (order.client == null || order.displayPeriod == null || order.advertText == null)
			return false;

		for (int i = 0; i < modelBllbrds.getRowCount(); ++i) {
			if ((Integer) modelBllbrds.getValueAt(i, 2) > 0) {
				IBillboard billboard = billboards.get((Integer) modelBllbrds.getValueAt(i, 0));
				if (billboard.addAdvertisement(order.advertText, order.displayPeriod, ordersIds) == false)
					return false;
				id = ordersIds;

				Advert ad = new Advert();
				ad.billboardId = (Integer) modelBllbrds.getValueAt(i, 0);
				ad.order = order;
				ad.orderId = id;
				orders.put(id, ad);
				order.client.setOrderId(id);
				ordersIds++;
			}
		}
		if (id == -1)
			return false;
		return true;
	}

	@Override
	public boolean withdrawOrder(int orderId) throws RemoteException {
		Advert ad = orders.get(orderId);
		if (ad == null)
			return false;
		if (billboards.get(ad.billboardId).removeAdvertisement(orderId) == false)
			return false;
		orders.remove(orderId);
		return true;
	}

	public void editSettings() {
		if (managerInterface != null) {
			unregist();
		}

		JTextField regField = new JTextField(5);
		JTextField managerField = new JTextField(5);
		JTextField nameField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.setSize(new Dimension(200, 400));
		myPanel.setPreferredSize(new Dimension(200, 200));

		myPanel.add(new JLabel("RMI Registry port:"));
		myPanel.add(regField);
		regField.setText(String.valueOf(registryPort));

		myPanel.add(new JLabel("Manager port:"));
		myPanel.add(managerField);
		managerField.setText(String.valueOf(managerPort));

		myPanel.add(new JLabel("Manager name:"));
		myPanel.add(nameField);
		nameField.setText(name);

		JOptionPane.showConfirmDialog(null, myPanel, "Manager settings", JOptionPane.PLAIN_MESSAGE);
		name = nameField.getText();
		registryPort = Integer.parseInt(regField.getText());
		managerPort = Integer.parseInt(managerField.getText());

		regist();
	}

	public boolean regist() {
		try {
			registry = LocateRegistry.createRegistry(registryPort);
			managerInterface = (IManager) UnicastRemoteObject.exportObject(this, managerPort);
			registry.bind(name, managerInterface);
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean unregist() {
		try {
			UnicastRemoteObject.unexportObject(managerInterface, true);
			managerInterface = null;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

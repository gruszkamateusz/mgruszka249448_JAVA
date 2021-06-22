package queuesyspack;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.JScrollPane;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class ManagerFrame extends JFrame {
	private static final long serialVersionUID = -459017155402031120L;
	private JTextField tfCategoryName;
	private JTextField tfPriority;
	private JList<TicketCategory> tickCatList;
	private DefaultListModel<TicketCategory> tickCatListModel;
	private List<ManagerListener> listeners;

	public ManagerFrame(DefaultListModel<TicketCategory> listModel) {
		setTitle("Manager");
		setLocation(300, 300);
		setSize(300, 300);
		setPreferredSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(300, 300));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		if(listModel == null) {
			tickCatListModel = new DefaultListModel<TicketCategory>();
		} else {
			tickCatListModel = listModel;
		}
		tickCatList = new JList<TicketCategory>(tickCatListModel);
		tickCatList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				TicketCategory cat = tickCatList.getSelectedValue();
				if(cat != null) {
					tfCategoryName.setText(cat.name);
					tfPriority.setText(Integer.toString(cat.priority));
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 131, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 225, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPane);
		scrollPane.setViewportView(tickCatList);
		
		JButton btnRemoveSelected = new JButton("Remove selected");
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSelected();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnRemoveSelected, 6, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, btnRemoveSelected, -20, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnRemoveSelected);
		
		tfCategoryName = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, tfCategoryName, 37, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, tfCategoryName, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tfCategoryName, -10, SpringLayout.WEST, scrollPane);
		getContentPane().add(tfCategoryName);
		tfCategoryName.setColumns(10);
		
		tfPriority = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, tfPriority, 25, SpringLayout.SOUTH, tfCategoryName);
		springLayout.putConstraint(SpringLayout.WEST, tfPriority, 0, SpringLayout.WEST, tfCategoryName);
		springLayout.putConstraint(SpringLayout.EAST, tfPriority, 0, SpringLayout.EAST, tfCategoryName);
		getContentPane().add(tfPriority);
		tfPriority.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Category name:");
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, tfCategoryName);
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, -6, SpringLayout.NORTH, tfCategoryName);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Priority:");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 6, SpringLayout.SOUTH, tfCategoryName);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel_1, 0, SpringLayout.WEST, tfCategoryName);
		getContentPane().add(lblNewLabel_1);
		
		JButton btnAddCat = new JButton("Add new");
		btnAddCat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNew();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnAddCat, 6, SpringLayout.SOUTH, tfPriority);
		springLayout.putConstraint(SpringLayout.WEST, btnAddCat, 0, SpringLayout.WEST, tfPriority);
		springLayout.putConstraint(SpringLayout.EAST, btnAddCat, 0, SpringLayout.EAST, tfPriority);
		getContentPane().add(btnAddCat);
		
		JButton btnEditCat = new JButton("Edit selected");
		btnEditCat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSelected();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnEditCat, 6, SpringLayout.SOUTH, btnAddCat);
		springLayout.putConstraint(SpringLayout.WEST, btnEditCat, 0, SpringLayout.WEST, tfCategoryName);
		springLayout.putConstraint(SpringLayout.EAST, btnEditCat, 0, SpringLayout.EAST, btnAddCat);
		getContentPane().add(btnEditCat);
		
		listeners = new ArrayList<ManagerListener>();
	}
	
	public TicketCategory addNew() {
		TicketCategory cat = new TicketCategory(
				tfCategoryName.getText(), 
				Integer.parseInt(tfPriority.getText()));
		tickCatListModel.addElement(cat);
		listeners.forEach((item)->{
			item.actionPerformed("Category added: " + cat.toString());
		});
		return cat;
	}
	
	public TicketCategory editSelected() {
		TicketCategory cat = tickCatList.getSelectedValue();
		cat.name = tfCategoryName.getText();
		cat.priority = Integer.parseInt(tfPriority.getText());
		tickCatList.updateUI();
		listeners.forEach((item)->{
			item.actionPerformed("Category edited: " + cat.toString());
		});
		return cat;
	}
	
	public TicketCategory removeSelected() {
		listeners.forEach((item)->{
			item.actionPerformed("Category removed: " + tickCatList.getSelectedValue().toString());
		});
		return tickCatListModel.remove(tickCatList.getSelectedIndex());
	}
	
	public DefaultListModel<TicketCategory> getTicketCategoryList() {
		return tickCatListModel;
	}
	
	public void setTicketCategoryList(DefaultListModel<TicketCategory> listModel) {
		tickCatListModel = listModel;
		tickCatList.setModel(tickCatListModel);
	}
	
	public void addManagerListener(ManagerListener listener) {
		listeners.add(listener);
	}
	
	public void removeManagerListener(ManagerListener listener) {
		listeners.remove(listener);
	}
}

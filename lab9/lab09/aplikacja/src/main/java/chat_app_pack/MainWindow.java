package chat_app_pack;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import chat_lib_pack.ConnectionListener;
import chat_lib_pack.SecureConnection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JTextField;
import java.awt.Font;

public class MainWindow extends JFrame implements ConnectionListener {
	private static final long serialVersionUID = 7447527366165867132L;
	private JTextArea inputTextArea;
	private JTextArea outputTextArea;
	private JTextField txtFldMyLstnPort;
	private JTextField txtFldOtherLstnPort;
	private JTextField txtFldOtherAddress;
	private JToggleButton btnMeListen;
	private JButton btnConnect;
	
	private SecureConnection secureConn;
	
	public MainWindow() {
		setTitle("ChatApp");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(300, 300);
		setSize(375, 380);
		setPreferredSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(300, 300));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				stopListen();
			}
		});
		
		JScrollPane scrollPaneOutput = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, scrollPaneOutput, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPaneOutput, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPaneOutput);
		
		outputTextArea = new JTextArea();
		outputTextArea.setEditable(false);
		scrollPaneOutput.setViewportView(outputTextArea);
		
		JScrollPane scrollPaneInput = new JScrollPane();
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneOutput, -30, SpringLayout.NORTH, scrollPaneInput);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPaneInput, -100, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPaneInput, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPaneInput, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPaneInput);
		
		inputTextArea = new JTextArea();
		scrollPaneInput.setViewportView(inputTextArea);
		
		JButton btnSend = new JButton("Send message");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(secureConn == null || secureConn.isConnected() == false) {
					JOptionPane.showMessageDialog(null, "Please, establish connection first!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String text = inputTextArea.getText();
				inputTextArea.setText("");
				outputTextArea.append("\n> " + text);
				try {
					secureConn.send(text);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.EAST, btnSend, -5, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneInput, -5, SpringLayout.NORTH, btnSend);
		springLayout.putConstraint(SpringLayout.WEST, btnSend, 5, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnSend, -5, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(btnSend);
		
		JLabel lblInput = new JLabel("Input:");
		springLayout.putConstraint(SpringLayout.NORTH, lblInput, 12, SpringLayout.SOUTH, scrollPaneOutput);
		springLayout.putConstraint(SpringLayout.WEST, lblInput, 10, SpringLayout.WEST, scrollPaneOutput);
		getContentPane().add(lblInput);
		
		JButton btnClearOutput = new JButton("Clear output");
		btnClearOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputTextArea.setText("");
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnClearOutput, 3, SpringLayout.SOUTH, scrollPaneOutput);
		springLayout.putConstraint(SpringLayout.EAST, btnClearOutput, -5, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnClearOutput);
		
		btnMeListen = new JToggleButton("Listen");
		btnMeListen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnMeListen.isSelected()) {
					listen();
				} else {
					stopListen();
				}
			}
		});
		getContentPane().add(btnMeListen);
		
		txtFldMyLstnPort = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, btnMeListen, -2, SpringLayout.NORTH, txtFldMyLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, btnMeListen, 20, SpringLayout.EAST, txtFldMyLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, txtFldMyLstnPort, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(txtFldMyLstnPort);
		txtFldMyLstnPort.setColumns(10);
		
		txtFldOtherLstnPort = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPaneOutput, 10, SpringLayout.SOUTH, txtFldOtherLstnPort);
		getContentPane().add(txtFldOtherLstnPort);
		txtFldOtherLstnPort.setColumns(10);
		
		txtFldOtherAddress = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, txtFldOtherAddress, 0, SpringLayout.NORTH, txtFldOtherLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, txtFldOtherAddress, 20, SpringLayout.EAST, txtFldOtherLstnPort);
		getContentPane().add(txtFldOtherAddress);
		txtFldOtherAddress.setColumns(10);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnConnect.getText() == "Connect") {
					connect();
				} else {
					disconnect();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnConnect, -2, SpringLayout.NORTH, txtFldOtherLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, btnConnect, 20, SpringLayout.EAST, txtFldOtherAddress);
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnConnect);
		
		JLabel lblMyLstnPort = new JLabel("Listen port");
		springLayout.putConstraint(SpringLayout.NORTH, txtFldMyLstnPort, 3, SpringLayout.SOUTH, lblMyLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, lblMyLstnPort, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblMyLstnPort);
		
		JLabel lblOtherLstnPort = new JLabel("Listen port");
		springLayout.putConstraint(SpringLayout.NORTH, txtFldOtherLstnPort, 2, SpringLayout.SOUTH, lblOtherLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, txtFldOtherLstnPort, 0, SpringLayout.WEST, lblOtherLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, lblOtherLstnPort, 0, SpringLayout.WEST, lblInput);
		getContentPane().add(lblOtherLstnPort);
		
		JLabel lblOtherAddress = new JLabel("Address");
		springLayout.putConstraint(SpringLayout.NORTH, lblOtherAddress, 0, SpringLayout.NORTH, lblOtherLstnPort);
		springLayout.putConstraint(SpringLayout.WEST, lblOtherAddress, 0, SpringLayout.WEST, txtFldOtherAddress);
		getContentPane().add(lblOtherAddress);
		
		JLabel lblOtherHost = new JLabel("Other host");
		springLayout.putConstraint(SpringLayout.NORTH, lblOtherHost, 15, SpringLayout.SOUTH, txtFldMyLstnPort);
		springLayout.putConstraint(SpringLayout.NORTH, lblOtherLstnPort, 3, SpringLayout.SOUTH, lblOtherHost);
		springLayout.putConstraint(SpringLayout.WEST, lblOtherHost, 0, SpringLayout.WEST, txtFldMyLstnPort);
		lblOtherHost.setFont(new Font("Tahoma", Font.PLAIN, 12));
		getContentPane().add(lblOtherHost);
		
		JLabel lblMe = new JLabel("Me");
		springLayout.putConstraint(SpringLayout.NORTH, lblMyLstnPort, 3, SpringLayout.SOUTH, lblMe);
		springLayout.putConstraint(SpringLayout.NORTH, lblMe, 5, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblMe, 10, SpringLayout.WEST, getContentPane());
		lblMe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		getContentPane().add(lblMe);
		
		
		txtFldMyLstnPort.setText("49999");
		txtFldOtherAddress.setText("127.0.0.1");
		txtFldOtherLstnPort.setText("49998");
	}
	
	public void listen() {
		try {
			stopListen();
			secureConn = new SecureConnection(Integer.parseInt(txtFldMyLstnPort.getText()));
			secureConn.addListener(this);
			secureConn.start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Some error during listening start.\nCheck address and ports.", "Error", JOptionPane.ERROR_MESSAGE);
			stopListen();
		}
	}
	
	public void onListen() {
		btnMeListen.setSelected(true);
		txtFldMyLstnPort.setEditable(false);
	}
	
	public void stopListen() {
		if(secureConn != null) {
			secureConn.stop();
		}
		secureConn = null;
	}
	
	public void onStopListen() {
		btnMeListen.setSelected(false);
		txtFldMyLstnPort.setEditable(true);
	}

	public void connect() {
		if(secureConn == null || secureConn.isListening() == false)
			listen();
		try {
			secureConn.connect(txtFldOtherAddress.getText(), Integer.parseInt(txtFldOtherLstnPort.getText()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Some error during establishing connection.\nCheck address and ports.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void onConnect() {
		btnConnect.setText("Disconnect");
		txtFldOtherAddress.setEditable(false);
		txtFldOtherLstnPort.setEditable(false);
	}
	
	public void disconnect() {
		if(secureConn != null && secureConn.isConnected()) {
			secureConn.disconnect();
		}
	}
	
	public void onDisconnect() {
		btnConnect.setText("Connect");
		txtFldOtherAddress.setEditable(true);
		txtFldOtherLstnPort.setEditable(true);
	}

	@Override
	public void messageReceived(byte[] rcvData, String host, int port) {
		outputTextArea.append("\n< " + new String(rcvData));
	}

	@Override
	public void stateChanged() {
		if(secureConn != null && secureConn.isConnected()) {
			onConnect();
		} else {
			onDisconnect();
		}
		
		if(secureConn != null && secureConn.isListening()) {
			onListen();
		} else {
			onStopListen();
		}
	}
	
	public static void main(String[] args) {
		new MainWindow().setVisible(true);
	}
}

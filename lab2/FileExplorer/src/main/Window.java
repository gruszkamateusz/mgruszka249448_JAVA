package main;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window {
	public JFrame frame;
	public TestPane panel = new TestPane();
    
	public Window() { 
		initialize();
	}

	private void initialize() {
		setFrame(new JFrame());
		
		getFrame().setBounds(100, 100, 500, 340);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        frame.setContentPane(panel);
        frame.setVisible(true);
		}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
		frame.setResizable(false);
	}
}

package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TestPane extends JPanel {

    private DefaultTreeModel model;
    private JTree tree;
    private String pathname = "";
    private String previousPathname = "";
    private String previousPathname2 = "";
    private String rootpath = "C:\\Users\\huawei\\Desktop\\Test";
    WeakReference<ImageIcon> weakReferenceImage;
    WeakReference<ImageIcon> weakReferenceImage2;
    WeakReference<String> weakReferenceText;
    WeakReference<String> weakReferenceText2;
    ImageIcon image;
    private JTextField textIsWeakReference;
    public TestPane() {
    	
        JPanel panel = new JPanel();
        panel.setBounds(259, 14, 180, 250);
        add(panel);

        File rootFile = new File(rootpath);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootFile);
        model = new DefaultTreeModel(root);
                setLayout(null);
        
                tree = new JTree();
                tree.setBounds(21, 14, 180, 250);
                add(tree);

        tree.setModel(model);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        
        tree.addTreeSelectionListener(new TreeSelectionListener() {
        	 
            public void valueChanged(TreeSelectionEvent e) {
            	
            	int iterator =  0;
               TreePath tp = e.getNewLeadSelectionPath();
               if (tp != null)	   	
               pathname = tp.getLastPathComponent().toString();

               int i = pathname.lastIndexOf('.');
               String fPath = pathname.replace('\\', '/');
               String everything = "";
               if (i > 0) {
                   if(pathname.substring(i+1).equals("txt")) {
                	   
                	   panel.removeAll();
                	   panel.repaint();

                	   if(pathname.equals(previousPathname2)) {
                		   everything = weakReferenceText2.get();
                		   textIsWeakReference.setText("Weak reference!!");
                	   }
                	   else {

                	   try {
                		   
                	   BufferedReader br = new BufferedReader(new FileReader(fPath));
                	   
                	   try {
                		   
                	       StringBuilder sb = new StringBuilder();
                	       String line = br.readLine();

                	       while (line != null) {
                	    	   
                	           		sb.append(line);
                	           		sb.append(System.lineSeparator());
                	           		line = br.readLine();
                	       		}
                	       			everything = sb.toString();
                	   		} finally {
                	   				br.close();
                	   			}
                	   		}catch(Exception e4) {
                		   
                	   		}
            		   	textIsWeakReference.setText("It's not weak reference");
            		   	
                	   }
                	   

                	   weakReferenceText = new WeakReference<String>(everything);
                	   
                	   JLabel label = new JLabel(everything, JLabel.CENTER);
                	   panel.add(label);
                   }
                   
                   if( pathname.substring(i+1).equals("jpg")||pathname.substring(i+1).equals("png") ) {
                	   
                	   panel.removeAll();
                	   panel.repaint();
                	   
                	   if(pathname.equals(previousPathname2)) {
                		   image = weakReferenceImage2.get();
                		   textIsWeakReference.setText("Weak reference!!");
                	   }
                	   else {
                    	   image = new ImageIcon(fPath, null);
                    	   textIsWeakReference.setText("It's not weak reference");
                	   }
                    	   weakReferenceImage2 = weakReferenceImage;
                    	   weakReferenceImage = new WeakReference<ImageIcon>(image);


                	   JLabel label = new JLabel(image, JLabel.CENTER);
                	   panel.add(label);
                	   
                   }
               }
               
               if(!previousPathname.equals(null)) {
                   previousPathname2 = previousPathname;
               }
               previousPathname = pathname;
               pathname = "";
            }
         });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(219, 14, 2, 2);
        add(scrollPane);

        JButton load = new JButton("Load");
        load.setBounds(64, 269, 84, 21);
        add(load);
        
        textIsWeakReference = new JTextField();
        textIsWeakReference.setHorizontalAlignment(SwingConstants.CENTER);
        textIsWeakReference.setEditable(false);
        textIsWeakReference.setBounds(259, 270, 180, 19);
        add(textIsWeakReference);
        textIsWeakReference.setColumns(10);
        

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                root.removeAllChildren();
                model.reload();
                File rootFile = (File) root.getUserObject();

                addFiles(rootFile, model, root);
                tree.expandPath(new TreePath(root));
                

            }
        });

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    protected void addFiles(File rootFile, DefaultTreeModel model, DefaultMutableTreeNode root) {

        for (File file : rootFile.listFiles()) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(file);
            model.insertNodeInto(child, root, root.getChildCount());
            if (file.isDirectory()) {
                addFiles(file, model, child);
            }
        }

    }
    /*private void rememberFile( ArrayList<String> previousPathnames, String toAdd ){
    	
    	if(previousPathnames.size()<3) {
        	for(String i : previousPathnames) {
        		if(i.equals(toAdd)) {
        			break;
        		}
        		else {
        			previousPathnames.add(toAdd);
        		}
        	}
    	}
    	else {
    		previousPathnames.remove(1);
    		previousPathnames.add(toAdd);
    	}

    }
    */
    
}

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

public class TestPane extends JPanel {

    private DefaultTreeModel model;
    private JTree tree;
    private String pathname = "";
    private ArrayList<String> oldPathnames;
    private String oldPathname = "";
    private String rootpath = "C:\\Users\\huawei\\Desktop\\Test";
    WeakReference<ImageIcon> weakReferenceImage;
    WeakReference<String> weakReferenceText;
    ImageIcon image;
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
                	   
                	   if(pathname.equals(oldPathname)) {
                		   everything = weakReferenceText.get();
                		   System.out.println("WeakTxt");
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
                	   }
                	   weakReferenceText = new WeakReference<String>(everything);
                	   JLabel label = new JLabel(everything, JLabel.CENTER);
                	   panel.add(label);
                   }
                   
                   if( pathname.substring(i+1).equals("jpg")||pathname.substring(i+1).equals("png") ) {
                	   
                	   panel.removeAll();
                	   panel.repaint();
                	   
                	   if(pathname.equals(oldPathname)) {
                		   image = weakReferenceImage.get();
                	   }
                	   else {
                		   ;
                    	   image = new ImageIcon(fPath, null);
                	   }
                	   weakReferenceImage = new WeakReference<ImageIcon>(image);
                	   JLabel label = new JLabel(image, JLabel.CENTER);
                	   panel.add(label);
                	   
                   }
               }
               oldPathname = pathname;
               pathname = "";
            }
         });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(219, 14, 2, 2);
        add(scrollPane);

        JButton load = new JButton("Load");
        load.setBounds(195, 269, 84, 21);
        add(load);
        

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
    /*private void rememberFile( ArrayList<String> oldPathnames, String toAdd ){
    	
    	if(oldPathnames.size()<3) {
        	for(String i : oldPathnames) {
        		if(i.equals(toAdd)) {
        			break;
        		}
        		else {
        			oldPathnames.add(toAdd);
        		}
        	}
    	}
    	else {
    		oldPathnames.remove(1);
    		oldPathnames.add(toAdd);
    	}

    }
    */
    
}

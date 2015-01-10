package com.perfect_fifths.levelbuilder;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Dialogs {
	
	static int[] showNewDialog(int[] defaults) {
		JTextField field1 = new JTextField(String.valueOf(defaults[0]));
		JTextField field2 = new JTextField(String.valueOf(defaults[1]));
		JTextField field3 = new JTextField(String.valueOf(defaults[2]));
		JTextField field4 = new JTextField(String.valueOf(defaults[3]));
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Width:"));
		panel.add(field1);
		panel.add(new JLabel("Height:"));
		panel.add(field2);
		panel.add(new JLabel("Grid Size:"));
		panel.add(field3);
		panel.add(new JLabel("Number of Layers:"));
		panel.add(field4);
		int result = JOptionPane.showConfirmDialog(null, panel, "Test",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			return new int[]{Integer.parseInt(field1.getText()), Integer.parseInt(field2.getText()), Integer.parseInt(field3.getText()), Integer.parseInt(field4.getText())};
		} else {
			return null;
		}
	}
	
	static File showSaveDialog(Component parent) {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Project Albert Area File", "paa");
	    chooser.setFileFilter(filter);
	    chooser.setApproveButtonText("Save");
	    int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File selectedFile = chooser.getSelectedFile();
	    	if (selectedFile.getName().endsWith(".paa")) {
	    		return selectedFile;
	    	} else {
	    		return new File(selectedFile + ".paa");
	    	}
	    }
	    return null;
	}

	static File showOpenTilesetDialog(Component parent) {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Image File", "jpg", "jpeg", "bmp", "wbmp", "gif", "png");
	    chooser.setFileFilter(filter);
	    chooser.setApproveButtonText("Open");
	    int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile();
	    }
	    return null;
	}
	
	static File showLevelOpenDialog(Component parent) {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Project Albert Area File", "paa");
	    chooser.setFileFilter(filter);
	    chooser.setApproveButtonText("Open");
	    int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile();
	    }
	    return null;
	}
	
	static int showChangeGridSizeDialog(Component parent) {
		JTextField field3 = new JTextField(String.valueOf("32"));
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Grid Size:"));
		panel.add(field3);
		int result = JOptionPane.showConfirmDialog(null, panel, "Test",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			return Integer.parseInt(field3.getText());
		} else {
			return 32;
		}
	}
}

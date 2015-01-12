package com.perfect_fifths.desktop.creation_studio.levelbuilder;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.perfect_fifths.asset_classes.SoundTile;
import com.perfect_fifths.asset_classes.TeleTile;
import com.perfect_fifths.asset_classes.Tile;

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
	
	static Tile showActionTileDialog(int invokingX, int invokingY) {
		DefaultComboBoxModel<String> typeList = new DefaultComboBoxModel<String>();
		JComboBox<String> typeComboBox = new JComboBox<String>(typeList);
		typeList.addElement("TeleTile");
		typeList.addElement("SoundTile");
		typeList.addElement("DiaTile");
		int result = JOptionPane.showConfirmDialog(null, typeComboBox, "New Action Tile",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			switch(typeComboBox.getSelectedIndex()) {
			case 0:
				JPanel panel = new JPanel();
				JTextField x = new JTextField();
				JTextField y = new JTextField();
				JTextField area = new JTextField();
				panel.add(new JLabel("X: "));
				panel.add(x);
				panel.add(new JLabel("Y: "));
				panel.add(y);
				panel.add(new JLabel("Area: "));
				panel.add(area);
				result = JOptionPane.showConfirmDialog(null, panel, "New TeleTile",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					return new TeleTile(invokingX, invokingY, Integer.parseInt(x.getText()), Integer.parseInt(y.getText()), Integer.parseInt(area.getText()));
				}
				break;
			case 1:
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Windows Wave Audio", "wav");
			    chooser.setFileFilter(filter);
			    chooser.setApproveButtonText("Open");
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	return new SoundTile(invokingX, invokingY, chooser.getSelectedFile());
			    }
				break;
			case 2:
				
				break;
			}
		}
		return null;
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

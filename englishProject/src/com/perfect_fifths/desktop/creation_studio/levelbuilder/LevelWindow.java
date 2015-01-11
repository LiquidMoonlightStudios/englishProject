package com.perfect_fifths.desktop.creation_studio.levelbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.perfect_fifths.asset_classes.Animation;
import com.perfect_fifths.asset_classes.Area;

public class LevelWindow extends JFrame {
	
	JScrollPane scroller;
	WorldView view;
	JComboBox<String> layerComboBox;
	DefaultComboBoxModel<String> layerList = new DefaultComboBoxModel<>();
	
	public LevelWindow(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		view = new WorldView();
		scroller = new JScrollPane(view);
		setPreferredSize(new Dimension((int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() * 2 / 3, (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight()));
		JPanel menuBar = new JPanel();
		menuBar.add(new JLabel("Layer: "));
		layerList.removeAllElements();
		for (String s : view.getLayerList()) {
			layerList.addElement(s);
		}
		layerComboBox = new JComboBox<String>(layerList);
		JButton newBtn = new JButton("New");
		JButton openBtn = new JButton("Open");
		JButton saveBtn = new JButton("Save");
		JButton saveV2 = new JButton("Save v2");
		JButton openV2 = new JButton("Open v2");
		layerComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				view.setActiveLayer(layerComboBox.getSelectedIndex());
			}
			
		});
		newBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] dimensions = Dialogs.showNewDialog(view.getDefaults());
				if (dimensions == null) {
					return;
				}
				view.create(dimensions[0], dimensions[1], dimensions[2], dimensions[3], false);
				layerComboBox.setModel(new DefaultComboBoxModel<String>(view.getLayerList()));
				repaint();
			}
		});
		openBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					view.create(FileOperations.openSeparate(Dialogs.showLevelOpenDialog(null)), false);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Failed to read file!");
					e1.printStackTrace();
				}
				layerList.removeAllElements();
				for (String s : view.getLayerList()) {
					layerList.addElement(s);
				}
			}
			
		});
		saveBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e)
            {
                try {
					if (FileOperations.saveSeparate(new FileOperations.FileContents(view.gridSize, view.getLayers()), Dialogs.showSaveDialog(null))) {
						JOptionPane.showMessageDialog(null, "Saved!", "Project Albert Level Builder", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Save failed", "Project Albert Level Builder", JOptionPane.ERROR_MESSAGE);
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
		});
		saveV2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Project Albert Area v2", "pa2");
			    chooser.setFileFilter(filter);
			    chooser.setApproveButtonText("Save");
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	try {
						FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile() + ".pa2");
						ObjectOutputStream out = new ObjectOutputStream(fos);
				        out.writeObject(view.getArea());
				        out.close();
				        fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
			}
			
		});
		openV2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Project Albert Area V2", "pa2");
			    chooser.setFileFilter(filter);
			    chooser.setApproveButtonText("Open");
			    int returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	try {
						FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
						ObjectInputStream in = new ObjectInputStream(fis);
				         view.create((Area) in.readObject(), false);
				         in.close();
				         fis.close();
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
			    }
			    layerList.removeAllElements();
				for (String s : view.getLayerList()) {
					layerList.addElement(s);
				}
			}
		});
		menuBar.add(layerComboBox);
		menuBar.add(newBtn);
		menuBar.add(openBtn);
		menuBar.add(saveBtn);
		menuBar.add(saveV2);
		menuBar.add(openV2);
		add(menuBar, BorderLayout.PAGE_START);
		add(scroller, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
}

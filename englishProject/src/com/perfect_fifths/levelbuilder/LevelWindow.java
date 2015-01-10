package com.perfect_fifths.levelbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class LevelWindow extends JFrame {
	
	JScrollPane scroller;
	WorldView view;
	JComboBox<String> layerComboBox;
	
	public LevelWindow(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		view = new WorldView();
		scroller = new JScrollPane(view);
		setPreferredSize(new Dimension((int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() * 2 / 3, (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight()));
		JPanel menuBar = new JPanel();
		menuBar.add(new JLabel("Layer: "));
		layerComboBox = new JComboBox<String>(view.getLayerList());
		JButton newBtn = new JButton("New");
		JButton openBtn = new JButton("Open");
		JButton saveBtn = new JButton("Save");
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
		menuBar.add(layerComboBox);
		menuBar.add(newBtn);
		menuBar.add(openBtn);
		menuBar.add(saveBtn);
		add(menuBar, BorderLayout.PAGE_START);
		add(scroller, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
}

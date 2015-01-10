package com.perfect_fifths.desktop.levelbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TileSetWindow extends JFrame {
	
	JScrollPane scroller;
	WorldView view;
	
	public TileSetWindow(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(800, 0);
		view = new WorldView();
		scroller = new JScrollPane(view);
		setPreferredSize(new Dimension((int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() / 3, (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight()));
		JPanel menuBar = new JPanel();
		JButton openBtn = new JButton("Open");
		openBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
            {
                try {
					view.create(new BufferedImage[] {ImageIO.read(Dialogs.showOpenTilesetDialog(null))}, Variables.getGridSize(), true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                view.repaint();
                System.out.println(view.getLayer(0).getWidth());
            }
		});
		menuBar.add(openBtn);
		add(menuBar, BorderLayout.PAGE_START);
		add(scroller, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
}

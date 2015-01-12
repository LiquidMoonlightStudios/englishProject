package com.perfect_fifths.desktop.creation_studio.asset_creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.perfect_fifths.asset_classes.Animation;
import com.perfect_fifths.asset_classes.Character;

public class CharacterCreator {

	static CharacterView characterView = new CharacterView();

	public static void main(String[] args) {
		CreatorWindow window = new CreatorWindow(
				"Project Albert Character Creator");
	}

	static class CreatorWindow extends JFrame {
		ArrayList<JLabel> animationLabels = new ArrayList<>();
		JPanel animationPicker = new JPanel();
		public CreatorWindow(String title) {
			super(title);
			this.setPreferredSize(new Dimension((int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth(), (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight()));
			addKeyListener(characterView);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			JButton save = new JButton("Save");
			JButton open = new JButton("Open");
			save.addKeyListener(characterView);
			open.addKeyListener(characterView);
			save.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Project Albert Animation", "pan");
				    chooser.setFileFilter(filter);
				    chooser.setApproveButtonText("Save");
				    int returnVal = chooser.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	try {
							FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile() + ".pac");
							ObjectOutputStream out = new ObjectOutputStream(fos);
					        out.writeObject(characterView.getCharacter());
					        out.close();
					        fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				    }
				}
			});
			open.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Project Albert Animation", "pac");
				    chooser.setFileFilter(filter);
				    chooser.setApproveButtonText("Open");
				    int returnVal = chooser.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	try {
							FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
							ObjectInputStream in = new ObjectInputStream(fis);
					         characterView.setCharacter((Character) in.readObject());
					         in.close();
					         fis.close();
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
				    }
				}
				
			});
			JPanel menuBar = new JPanel();
			menuBar.add(save);
			menuBar.add(open);
			menuBar.addKeyListener(characterView);
			GridLayout animationPickerLayout = new GridLayout(0, 3);
			animationPicker.setLayout(animationPickerLayout);
			addRow("Static");
			addRow("Up");
			addRow("Down");
			addRow("Left");
			addRow("Right");
			addRow("Auxiliary 1");
			addRow("Auxiliary 2");
			addRow("Auxiliary 3");
			addRow("Auxiliary 4");
			addRow("Auxiliary 5");
			this.add(menuBar, BorderLayout.PAGE_START);
			this.add(animationPicker, BorderLayout.LINE_START);
			this.add(characterView, BorderLayout.CENTER);
			this.pack();
			this.setVisible(true);
			characterView.start();
		}
		
		public void addRow(String label) {
			animationPicker.add(new JLabel(label + ": "));
			JLabel locText = new JLabel("None");
			animationPicker.add(locText);
			animationLabels.add(locText);
			JButton browseBtn = new JButton("Browse");
			int index = animationLabels.size() - 1;
			browseBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Project Albert Animation", "pan");
				    chooser.setFileFilter(filter);
				    chooser.setApproveButtonText("Open");
				    int returnVal = chooser.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	try {
							FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
							ObjectInputStream in = new ObjectInputStream(fis);
					        characterView.changeAnimation((Animation) in.readObject(), index);
					        animationLabels.get(index).setText((chooser.getSelectedFile().getName()));
					        in.close();
					        fis.close();
						} catch (IOException | ClassNotFoundException f) {
							f.printStackTrace();
						}
				    }
				}
			});
			browseBtn.addKeyListener(characterView);
			animationPicker.add(browseBtn);
		}
	}

	static class CharacterView extends JPanel implements KeyListener {
		Character character = new Character();
		boolean run = true;

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (run) {
					update();
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
					}
				}
			}
		});

		public CharacterView() {
			super();
		}

		public void start() {
			addKeyListener(this);
			t.start();
		}

		public void setSpeed(int animation, int fps) {
			character.setAnimationSpeed(animation, fps);
		}

		public void changeAnimation(int animation) {
			character.changeAnimation(animation);
		}
		
		public void changeAnimation(Animation a, int index) {
			character.changeAnimation(a, index);
		}

		public void update() {
			character.update();
			repaint();
		}
		
		public Character getCharacter() {
			return character;
		}
		
		public void setCharacter(Character c) {
			character = c;
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			character.paint(g);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case 38:
			case 87:
				characterView.changeAnimation(Character.UP);
				break;
			case 40:
			case 83:
				characterView.changeAnimation(Character.DOWN);
				break;
			case 37:
			case 65:
				characterView.changeAnimation(Character.LEFT);
				break;
			case 39:
			case 68:
				characterView.changeAnimation(Character.RIGHT);
				break;
			case 49:
				characterView.changeAnimation(Character.AUX1);
				break;
			case 50:
				characterView.changeAnimation(Character.AUX2);
				break;
			case 51:
				characterView.changeAnimation(Character.AUX3);
				break;
			case 52:
				characterView.changeAnimation(Character.AUX4);
				break;
			case 53:
				characterView.changeAnimation(Character.AUX5);
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			characterView.changeAnimation(Character.STATIC);
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}
}

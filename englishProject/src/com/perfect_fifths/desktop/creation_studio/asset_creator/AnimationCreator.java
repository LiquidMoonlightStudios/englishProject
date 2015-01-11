package com.perfect_fifths.desktop.creation_studio.asset_creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.perfect_fifths.asset_classes.Animation;

public class AnimationCreator {
	
	static AnimationView runningAnimation = new AnimationView(new Animation(), true);

	public static void main(String[] args) {
		CreatorWindow window = new CreatorWindow("Project Albert Animation Creator");
	}
	
	static class CreatorWindow extends JFrame {
		
		static JButton addFrameBtn = new JButton("Add Frame");
		static JButton deleteFrameBtn = new JButton("Delete Frame");
		static JTextField framerate = new JTextField("7");
		static JPanel menuBar = new JPanel();
		static JButton openBtn = new JButton("Open");
		static JButton saveBtn = new JButton("Save");
		static JTextField index = new JTextField("1");
		
		public CreatorWindow(String title) {
			super(title);
			this.setPreferredSize(new Dimension((int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth(), (int) GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight()));
			addFrameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Image File", "jpg", "jpeg", "bmp", "wbmp", "gif", "png");
				    chooser.setFileFilter(filter);
				    chooser.setApproveButtonText("Open");
				    int returnVal = chooser.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	try {
				    		int indexInt = Integer.parseInt(index.getText());
							runningAnimation.addFrame(ImageIO.read(chooser.getSelectedFile()), indexInt - 1);
							index.setText(String.valueOf(indexInt + 1));
						} catch (IOException e) {
							e.printStackTrace();
						}
				    }
				}
			});
			deleteFrameBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					runningAnimation.deleteFrame(Integer.parseInt(index.getText()) - 1);
				}
			});
			framerate.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					runningAnimation.setSpeed(Integer.parseInt(framerate.getText()));
				}
				
			});
			openBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
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
					         runningAnimation.changeAnimation((Animation) in.readObject());
					         in.close();
					         fis.close();
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
				    }
				}
				
			});
			saveBtn.addActionListener(new ActionListener() {

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
							FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile() + ".pan");
							ObjectOutputStream out = new ObjectOutputStream(fos);
					        out.writeObject(runningAnimation.getAnimation());
					        out.close();
					        fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				    }
				}
				
			});
			framerate.setPreferredSize(new Dimension(100, 20));
			index.setPreferredSize(new Dimension(100, 20));
			menuBar.add(new JLabel("FPS:"));
			menuBar.add(framerate);
			menuBar.add(new JLabel("Index: "));
			menuBar.add(index);
			menuBar.add(addFrameBtn);
			menuBar.add(deleteFrameBtn);
			menuBar.add(openBtn);
			menuBar.add(saveBtn);
			this.add(menuBar, BorderLayout.PAGE_START);
			this.add(runningAnimation);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.pack();
			this.setVisible(true);
			runningAnimation.start();
		}
	}
	
	static class AnimationView extends JPanel {
		Animation animation = new Animation();
		boolean run = true;
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (run) {
					update();
					try {
						Thread.sleep(17);
					} catch (InterruptedException e) {		}
				}
			}
		});
		
		public AnimationView() {
			super();
		}
		
		public void deleteFrame(int index) {
			animation.deleteFrame(index);
		}

		public void start() {
			t.start();
			animation.setSpeed(7);
		}
		
		public AnimationView(Animation a, boolean playing) {
			animation = a;
			if (playing) {
				animation.play();
			} else {
				animation.stop();
			}
		}
		
		public void setSpeed(int fps) {
			animation.setSpeed(fps);
		}
		
		public void changeAnimation(Animation a) {
			animation = a;
		}
		
		public Animation getAnimation() {
			return animation;
		}
		
		public void addFrame(BufferedImage i) {
			animation.addFrame(i);
		}
		
		public void addFrame(BufferedImage image, int index) {
			animation.addFrame(image, index);
		}
		
		public void update() {
			animation.update();
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0,  0, getWidth(), getHeight());
			int staticY = (int) animation.paint(g, 0, 0).getHeight();
			int currentX = 0;
			for (BufferedImage i : animation.getFrames()) {
				g.drawImage(i, currentX, staticY, null);
				g.drawLine(currentX, staticY, currentX, staticY + i.getHeight());
				currentX += i.getWidth();
			}
		}
	}
}

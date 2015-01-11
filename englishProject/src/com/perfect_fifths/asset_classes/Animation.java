package com.perfect_fifths.asset_classes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Animation implements Serializable {
	private static final long serialVersionUID = -2160548328324005846L;
	transient BufferedImage[] frames;
	transient int currentFrame = 0;
	int delay = 16;
	transient boolean isPlaying = true;

	transient Long lastUpdate = 0L;
	
	public Animation() {
		super();
		frames = new BufferedImage[0];
	}
	
	public Animation(BufferedImage[] frames) {
		super();
		this.frames = frames;
	}
	
	public Animation(BufferedImage[] frames, int delay, boolean autoPlay) {
		super();
		this.frames = frames;
		this.delay = delay;
		this.isPlaying = autoPlay;
	}

	public Animation(BufferedImage[] frames, boolean isPlaying) {
		super();
		this.frames = frames;
		this.isPlaying = isPlaying;
	}

	public Animation(BufferedImage[] frames, int delay) {
		super();
		this.frames = frames;
		this.delay = delay;
	}

	public void update() {
		if (isPlaying && System.nanoTime() - lastUpdate >= delay) {
			lastUpdate = System.nanoTime();
			if (currentFrame < frames.length - 1) {
				currentFrame++;
			} else {
				currentFrame = 0;
			}
		}
	}
	
	public void changeCurrentFrame(int frame) {
		currentFrame = frame;
	}
	
	public void setSpeed(int fps) {
		delay = 1000000000/fps;
	}
	
	public void addFrame(BufferedImage frame) {
		BufferedImage[] temp = frames;
		frames = new BufferedImage[temp.length + 1];
		for (int i = 0; i < temp.length; i ++) {
			frames[i] = temp[i];
		}
		frames[temp.length] = frame;
	}
	
	public void addFrame(BufferedImage frame, int index) {
		if (index > frames.length + 1) {
			index = frames.length + 1;
		}
		BufferedImage[] temp = frames;
		frames = new BufferedImage[temp.length + 1];
		for (int i = 0; i < index; i ++) {
			frames[i] = temp[i];
		}
		frames[index] = frame;
		for (int i = index + 1; i < frames.length; i++) {
			frames[i] = temp[i - 1];
		}
	}
	
	public void play() {
		isPlaying = true;
	}
	
	public void pause() {
		isPlaying = false;
	}
	
	public void stop() {
		isPlaying = false;
		currentFrame = 0;
	}
	
	public BufferedImage[] getFrames() {
		return frames;
	}
	
	public Dimension paint(Graphics g, int x, int y) {
		if (frames.length < 1)
			return new Dimension(0, 0);
		g.drawImage(frames[currentFrame], x, y, null);
		return new Dimension(frames[currentFrame].getWidth(), frames[currentFrame].getHeight());
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(frames.length); // how many images are serialized?
        for (BufferedImage eachImage : frames) {
        	ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        	ImageIO.write(eachImage, "png", bufferStream);
            byte[] bufferedBytes = bufferStream.toByteArray();
            out.writeObject(bufferedBytes);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCount = in.readInt();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>(imageCount);
        for (int i=0; i<imageCount; i++) {
            images.add(ImageIO.read(new ByteArrayInputStream((byte[]) in.readObject())));
        }
        frames = images.toArray(new BufferedImage[images.size()]);
        currentFrame = 0;
        isPlaying = true;
        lastUpdate = 0L;
    }

	public void deleteFrame(int index) {
		if (index > frames.length + 1) {
			index = frames.length + 1;
		}
		BufferedImage[] temp = frames;
		frames = new BufferedImage[temp.length - 1];
		for (int i = 0; i < index; i++) {
			frames[i] = temp[i];
		}
		for (int i = index; i < frames.length; i++) {
			frames[i] = temp[i + 1];
		}
	}
}

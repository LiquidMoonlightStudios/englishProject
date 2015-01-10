package com.perfect_fifths.desktop.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Date;

public class Animation {
	BufferedImage[] frames;
	int currentFrame = 0;
	int delay = 16;
	boolean isPlaying = true;

	Date lastUpdate = new Date();
	
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
		Date currentDate = new Date();
		if (currentDate.compareTo(lastUpdate) >= delay && isPlaying) {
			lastUpdate = new Date();
			currentFrame++;
		}
	}
	
	public void changeCurrentFrame(int frame) {
		currentFrame = frame;
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
	
	public void paint(Graphics g, int x, int y) {
		g.drawImage(frames[currentFrame], x, y, null);
	}
}

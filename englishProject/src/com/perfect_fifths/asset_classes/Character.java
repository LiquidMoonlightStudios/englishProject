package com.perfect_fifths.asset_classes;

import java.awt.Graphics;
import java.io.ObjectInputStream;

public class Character implements java.io.Serializable {
	private static final long serialVersionUID = 8729574840294597942L;
	public static final int STATIC = 0, UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4, AUX1 = 5, AUX2 = 6, AUX3 = 7, AUX4 = 8, AUX5 = 9;
	transient int x = 0;
	transient int y = 0;
	Animation[] animations = new Animation[] {new Animation(), new Animation(), new Animation(), new Animation(), new Animation(), new Animation(), new Animation(), new Animation(), new Animation(), new Animation()};
	int currentAnimation = 0;
	
	public Character() {	}
	
	public Character(int x, int y, Animation[] animations, int currentAnimation) {
		this.x = x;
		this.y = y;
		this.animations = animations;
		this.currentAnimation = currentAnimation;
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void changeAnimation(int index) {
		currentAnimation = index;
	}
	
	public void changeAnimation(Animation a, int index) {
		animations[index] = a;
	}
	
	public void update() {
		for (Animation a : animations) {
			a.update();
		}
	}

	public void paint(Graphics g) {
		if (animations.length < 1 || currentAnimation >= animations.length) 
			return;
		animations[currentAnimation].paint(g, x, y);
	}
	
	private void readObject(ObjectInputStream in) {
		x = 0;
		y = 0;
	}

	public void setAnimationSpeed(int animation, int fps) {
		animations[animation].setSpeed(fps);
	}
}

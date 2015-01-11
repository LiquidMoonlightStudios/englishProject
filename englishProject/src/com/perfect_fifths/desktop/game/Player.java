package com.perfect_fifths.desktop.game;

import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.perfect_fifths.asset_classes.Animation;

public class Player implements java.io.Serializable {
	private static final long serialVersionUID = 8729574840294597942L;
	public static final int STATIC = 0, UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
	transient int x;
	transient int y;
	Animation[] animations;
	int currentAnimation = 0;
	
	public Player(int x, int y, Animation[] animations, int currentAnimation) {
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

	public void paint(Graphics g) {
		animations[currentAnimation].paint(g, x, y);
	}
	
	private void readObject(ObjectInputStream in) {
		x = 0;
		y = 0;
	}
}

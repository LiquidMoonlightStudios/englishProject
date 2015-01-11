package com.perfect_fifths.desktop.game;

import java.awt.Graphics;

import com.perfect_fifths.asset_classes.Animation;

public class Player {
	int x;
	int y;
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
}

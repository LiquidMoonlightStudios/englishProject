package com.perfect_fifths.asset_classes;

import java.io.Serializable;

import com.perfect_fifths.desktop.game.Player;

public class TeleTile extends Tile implements Serializable {
	
	int teleX, teleY, area;
	
	public TeleTile(int x, int y, int teleX, int teleY, int area) {
		super(x, y);
		this.teleX = teleX;
		this.teleY = teleY;
		this.area = area;
	}
	
	private static final long serialVersionUID = -2922624174071794672L;
	
	@Override
	public void doAction() {
		Player.character.moveTo(teleX, teleY);
		Player.area = area;
	}

	@Override
	public String getType() {
		return "Teleport";
	}
}

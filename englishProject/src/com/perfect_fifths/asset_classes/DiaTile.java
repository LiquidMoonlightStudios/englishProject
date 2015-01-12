package com.perfect_fifths.asset_classes;

import com.perfect_fifths.desktop.game.Player;

public class DiaTile extends Tile implements java.io.Serializable {
	
	Dialog dialog;

	public DiaTile(int x, int y, Dialog dialog) {
		super(x, y);
		this.dialog = dialog;
	}

	@Override
	public void doAction() {
		Player.visibleDialog = dialog;
	}

	@Override
	public String getType() {
		return "Dialog";
	}
	
	

}

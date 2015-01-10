package com.perfect_fifths.desktop.levelbuilder;

public class Boot {

	public static void main(String[] args) {
		LevelWindow levelWindow = new LevelWindow("Project Albert Level Builder");
		TileSetWindow tileSetWindow = new TileSetWindow("Project Albert Level Builder");
		tileSetWindow.setLocation((int) (levelWindow.getWidth() + levelWindow.getLocation().getX()), 0);
	}
}

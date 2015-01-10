package com.perfect_fifths.desktop.creation_studio.levelbuilder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Variables {

	private static int gridSize = 50;
	private static ArrayList<Runnable> gridSizeListeners = new ArrayList<>();
	public static BufferedImage selection;
	
	public static void setGridSize(int newSize) {
		gridSize = newSize;
		for (int i = 0; i < gridSizeListeners.size(); i++) {
			Thread worker = new Thread(gridSizeListeners.get(i));
			worker.start();
		}
	}
	
	public static int getGridSize() {
		return gridSize;
	}
	
	public static void addGridSizeListener(Runnable r) {
		gridSizeListeners.add(r);
	}
}

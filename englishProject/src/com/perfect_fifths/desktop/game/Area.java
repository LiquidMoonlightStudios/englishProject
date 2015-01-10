package com.perfect_fifths.desktop.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.perfect_fifths.desktop.creation_studio.levelbuilder.FileOperations.FileContents;

public class Area {
	BufferedImage belowPlayer;
	BufferedImage abovePlayer;
	
	public Area(BufferedImage[] layers, int playerLayer) {
		Graphics g = belowPlayer.createGraphics();
		for (int i = 0; i <= playerLayer; i++) {
			g.drawImage(layers[i], 0, 0, null);
		}
		g = abovePlayer.createGraphics();
		for (int i = playerLayer + 1; i <= layers.length; i++) {
			g.drawImage(layers[i], 0, 0, null);
		}
	}
	
	public Area(File file) throws IOException {
		FileContents res = new FileContents();
		Scanner scanner = new Scanner(file);
		BufferedImage[] layers = new BufferedImage[scanner.nextInt()];
		res.tileSize = scanner.nextInt();
		scanner.close();
		for (int i = 0; i < layers.length; i++) {
			layers[i] = ImageIO.read(new File(file.getParentFile(), "/" + getFileName(file) + i + ".pal"));
		}
		
		Graphics g = belowPlayer.createGraphics();
		for (int i = 0; i <= 2; i++) {
			g.drawImage(layers[i], 0, 0, null);
		}
		g = abovePlayer.createGraphics();
		for (int i = 2 + 1; i <= layers.length; i++) {
			g.drawImage(layers[i], 0, 0, null);
		}
	}
	
	private static String getFileName(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    return name.substring(0, pos);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void paint(Graphics g, Player player) {
		g.drawImage(belowPlayer, 0, 0, null);
		player.paint(g);
		g.drawImage(abovePlayer, 0, 0, null);
	}
}

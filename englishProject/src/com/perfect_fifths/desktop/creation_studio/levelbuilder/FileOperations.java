package com.perfect_fifths.desktop.creation_studio.levelbuilder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class FileOperations {

	public static boolean saveSeparate(FileContents data, File file) {
		try {
		if (file.isDirectory() || data.layers.length <= 0) {
			throw new IllegalArgumentException();
		}
		
		for (int i = 0; i < data.layers.length; i++) {
			ImageIO.write(data.layers[i], "PNG", new File(file.getParentFile(), "/" + getFileName(file) + i + ".pal"));
		}
		
		PrintStream fw = new PrintStream(file);
		fw.println(String.valueOf(data.layers.length));
		fw.println(String.valueOf(data.tileSize));
		fw.flush();
		fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static FileContents openSeparate(File file) throws IOException, IllegalArgumentException {
		FileContents res = new FileContents();
		Scanner scanner = new Scanner(file);
		BufferedImage[] layers = new BufferedImage[scanner.nextInt()];
		res.tileSize = scanner.nextInt();
		
		for (int i = 0; i < layers.length; i++) {
			layers[i] = ImageIO.read(new File(file.getParentFile(), "/" + getFileName(file) + i + ".pal"));
		}
		res.layers = layers;
		res.calcSize();
		scanner.close();
		return res;
	}
	
	public static String getFileName(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    return name.substring(0, pos);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public static class FileContents {
		public int tileSize;
		public int width;
		public int height;
		public BufferedImage[] layers;
		
		public FileContents(int tileSize, BufferedImage[] layers) {
			this.tileSize = tileSize;
			this.layers = layers;
			calcSize();
		}
		
		public FileContents() {
			tileSize = 0;
			width = 0;
			height = 0;
			layers = new BufferedImage[0];
		}
		
		public void calcSize() {
			width = 0;
			height = 0;
			for (BufferedImage layer : layers) {
				int curHeight = layer.getHeight();
				int curWidth = layer.getWidth();
				if (curHeight > height) {
					height = curHeight;
				}
				if (curWidth > width) {
					width = curWidth;
				}
			}
		}
	}
}

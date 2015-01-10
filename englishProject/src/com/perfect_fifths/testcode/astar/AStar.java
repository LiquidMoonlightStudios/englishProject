package com.perfect_fifths.testcode.astar;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AStar {
	
	
	static int failCounter = 0;
	static int tileSize = 50;
	
	static int width = 10;
	static int height = 10;
	
	static Canvas canvas = new Canvas();
	
	static ArrayList<Tile> path = new ArrayList<>();
	
	static Tile[] tiles;
	/*static Tile[] blockedTiles = new Tile[] { new Tile(2, 1), new Tile(3, 1),
			new Tile(4, 1), new Tile(5, 1), new Tile(5, 2), new Tile(5, 3), new Tile(5, 4),
			new Tile(5, 5), new Tile(5, 6), new Tile(5, 7), new Tile(4, 7), new Tile(3, 7),
			new Tile(2, 7)};*/
	static Tile[] blockedTiles = new Tile[] { new Tile(0, 1), new Tile(1, 1), new Tile(1, 2), new Tile(2, 2), new Tile(3, 2),
		new Tile(4, 2), new Tile(5, 2), new Tile(5, 3), new Tile(5, 4),
		new Tile(5, 5), new Tile(5, 6), new Tile(6, 7), new Tile(6, 6), new Tile(6, 8), new Tile(5, 8), new Tile(4, 8), new Tile(4, 6), new Tile(3, 6),
		new Tile(2, 6)};
	static Tile start = new Tile(1, 4);
	static Tile end = new Tile(8, 4);

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(width * tileSize + 50, height * tileSize + 50);
		jframe.add(canvas);
		jframe.setVisible(true);
		
		boolean added = false;
		ArrayList<Tile> newTiles = new ArrayList<>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (Tile tile : blockedTiles) {
					if (tile.x == i && tile.y == j) {
						newTiles.add(new Tile(i, j, false));
						added = true;
						break;
					}
				}

				if (!added)
					newTiles.add(new Tile(i, j));
				added = false;
			}
		}
		tiles = newTiles.toArray(new Tile[newTiles.size()]);
		Thread worker = new Thread(new Runnable() {

			@Override
			public void run() {
				calculatePath();
			}
		});
		worker.start();
	}

	public static void calculatePath() {
		Tile current = start;
		while (true) {
			boolean success = true;
			try {
			if (current.x == end.x && current.y == end.y) {
				System.out.println("Arrived");
				break;
			}
			current = current.getNextNode(tiles, end);
			path.add(current);
			canvas.repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} catch (NullPointerException e) {
				System.err.println("Null Pointer1");
				failCounter += (failCounter < 2) ? 3 : 2;
				current = path.get(path.size() - failCounter);
				success = false;
			}
			if (success) {
				failCounter = 0;
			}
		}
	}
	
	public static class Canvas extends JPanel {
		@Override
        protected void paintComponent(Graphics g) {
			g.setColor(Color.BLACK);
            for (Tile tile : tiles) {
            	g.drawRect(tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
            }
            for (Tile tile : blockedTiles) {
            	g.fillRect(tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
            }
            g.setColor(Color.BLUE);
            for (int i = 0; i < path.size(); i++) {
				path.removeAll(Collections.singleton(null));
            	g.fillRect(path.get(i).x * tileSize, path.get(i).y * tileSize, tileSize, tileSize);
            }
            g.setColor(Color.GREEN);
            g.fillRect(start.x * tileSize, start.y * tileSize, tileSize, tileSize);
            g.setColor(Color.RED);
            g.fillRect(end.x * tileSize, end.y * tileSize, tileSize, tileSize);
            g.setColor(Color.ORANGE);
            for (Tile tile : path) {
            	try {
            		g.drawLine((tile.x * tileSize) + (tileSize / 2), (tile.y * tileSize) + (tileSize / 2), (tile.parent.x * tileSize) + (tileSize / 2), (tile.parent.y * tileSize) + (tileSize / 2));
            	} catch (NullPointerException e) {
            		System.err.println("Null pointer");
            	}
            }
        }
	}
}

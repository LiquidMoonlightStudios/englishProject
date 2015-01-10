package com.perfect_fifths.testcode.astar;

public class Tile {
	public int x, y;
	boolean blocked;
	boolean open = true;
	int g;
	Tile parent = null;

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		blocked = false;
	}

	public Tile(int x, int y, boolean open) {
		this.x = x;
		this.y = y;
		this.open = open;
	}

	public int getF(Tile current, Tile end) {
		if (end == this || current == this || blocked)
			return -1;

		int h = (Math.abs(x - end.x) + Math.abs(y - end.y)) * 10;
		g = (int) Math.sqrt(Math.pow(x - current.x, 2)
				+ Math.pow(y - current.y, 2)) * 10;
		int f = g + h;

		if (g > 14)
			return -1;

		parent = current;
		return f;
	}

	public int getG(Tile current) {
		return (int) Math.sqrt(Math.pow(x - current.x, 2)
				+ Math.pow(y - current.y, 2)) * 10;
	}

	public Tile getNextNode(Tile[] tiles, Tile end) {
		open = false;
		int lowestF = -1;
		Tile lowestTile = null;
		for (Tile tile : tiles) {
			//if (tile.parent == null || tile.g < tile.getG(this)) {
				int f = tile.getF(this, end);
				if ((lowestF == -1 || f < lowestF) && f > 0 && tile.open) {
					lowestF = f;
					lowestTile = tile;
				}
			//}
		}

		if (lowestTile != null)
			return lowestTile;
		return lowestTile;

	}

	@Override
	public String toString() {
		return "Tile(" + x + ", " + y + ")";//, open = " + open + ") parent = " + parent;
	}
}
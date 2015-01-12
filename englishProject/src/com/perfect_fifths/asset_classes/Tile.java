package com.perfect_fifths.asset_classes;

public abstract class Tile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4683807412878349404L;
	int x, y;
	
	public abstract void doAction();
	public abstract String getType();

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

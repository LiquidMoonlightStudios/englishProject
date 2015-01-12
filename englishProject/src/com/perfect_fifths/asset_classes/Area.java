package com.perfect_fifths.asset_classes;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Area implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1258948642682265718L;
	transient BufferedImage[] layers;
	int tileSize;
	boolean[][] walkable;
	ArrayList<Tile> actionTiles = new ArrayList<>();
	
	public Area(BufferedImage[] layers, int tileSize, boolean[][] walkable, Tile[] actionTiles) {
		this.layers = layers;
		this.tileSize = tileSize;
		this.walkable = walkable;
		for (Tile t : actionTiles) {
			this.actionTiles.add(t);
		}
	}

	public BufferedImage[] getLayers() {
		return layers;
	}

	public void setLayers(BufferedImage[] layers) {
		this.layers = layers;
	}

	public int getTileSize() {
		return tileSize;
	}
	
	public BufferedImage getLayer(int index) {
		return layers[index];
	}
	
	public Dimension getPixelSize() {
		int highest = 0;
		int widest = 0;
		for (BufferedImage layer : layers) {
			int height = layer.getHeight();
			int width = layer.getWidth();
			if (height > highest) {
				highest = height;
			}
			if (width > widest) {
				widest = width;
			}
		}
		
		return new Dimension(widest, highest);
	}
	
	public Dimension getGridSize() {
		Dimension pixelSize = getPixelSize();
		pixelSize.width /= tileSize;
		pixelSize.height /= tileSize;
		return pixelSize;
	}
	
	public Tile[] getActionTiles() {
		return actionTiles.toArray(new Tile[actionTiles.size()]);
	}
	
	public void addActionTile(Tile t) {
		actionTiles.add(t);
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(layers.length);
        for (BufferedImage eachImage : layers) {
        	ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        	ImageIO.write(eachImage, "png", bufferStream);
            byte[] bufferedBytes = bufferStream.toByteArray();
            out.writeObject(bufferedBytes);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCount = in.readInt();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>(imageCount);
        for (int i=0; i<imageCount; i++) {
            images.add(ImageIO.read(new ByteArrayInputStream((byte[]) in.readObject())));
        }
        layers = images.toArray(new BufferedImage[images.size()]);
    }
}

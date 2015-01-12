package com.perfect_fifths.desktop.creation_studio.levelbuilder;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.perfect_fifths.asset_classes.Area;
import com.perfect_fifths.asset_classes.Tile;

public class WorldView extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	boolean real = false;
	public static final int BACKGROUND = 0, BACKGROUND_DETAILS = 1, FOREGROUND = 2, FOREGROUND_DETAILS = 3;
	BufferedImage[] layers;
	boolean[][] walkable;
	ArrayList<Tile> actionTiles = new ArrayList<>();
	boolean isShiftPressed = false;
	boolean isControlPressed = false;
	boolean settingTo = true;
	int gridSize;
	int gridWidth, gridHeight;
	int pixelWidth, pixelHeight;
	boolean tileset;
	int selectedTileX = 0, selectedTileY = 0;
	int activeLayer = 0;
	
	public WorldView() {
		super();
		Variables.addGridSizeListener(new Runnable() {
			@Override
			public void run() {
				gridSize = Variables.getGridSize();
				repaint();
			}
		});
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}
	
	public int[] getDefaults() {
		return real ? new int[]{gridWidth, gridHeight, gridSize, layers.length} : new int[]{10, 10, 50, 4};
	}
	
	public void create(int width, int height, int gridSize, int layerCount, boolean isTileset) {
		if (width <= 0 && height <= 0) {
			return;
		}
		real = true;
		gridWidth = width;
		gridHeight = height;
		walkable = new boolean[gridWidth][gridHeight];
		pixelWidth = gridSize * gridWidth;
		pixelHeight = gridSize * gridHeight;
		setSize(pixelWidth, pixelHeight);
		setPreferredSize(new Dimension(pixelWidth, pixelHeight));
		layers = new BufferedImage[layerCount];
		Variables.setGridSize(gridSize);
		for (int i = 0; i < layerCount; i++) {
			clearLayer(i);
		}
		this.tileset = isTileset;
	}
	
	public void create(BufferedImage[] layers, int gridSize, boolean isTileset) {
		if (layers.length <= 0) {
			return;
		}
		real = true;
		Variables.setGridSize(gridSize);
		int widest = 0;
		int highest = 0;
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
		pixelWidth = widest;
		pixelHeight = highest;
		gridWidth = widest / gridSize;
		gridHeight = highest / gridSize;
		create(gridWidth, gridHeight, gridSize, layers.length, isTileset);
		this.layers = layers;
		repaint();
	}
	
	public void create(Area area, boolean isTileset) {
		create(area.getLayers(), area.getTileSize(), isTileset);
	}
	
	public void create(FileOperations.FileContents level, boolean isTileset) {
		if (level.layers.length <= 0) {
			return;
		}
		real = true;
		Variables.setGridSize(gridSize);
		pixelWidth = level.width;
		pixelHeight = level.height;
		gridWidth = level.width / level.tileSize;
		gridHeight = level.height / level.tileSize;
		create(gridWidth, gridHeight, level.tileSize, level.layers.length, isTileset);
		layers = level.layers;
		repaint();
	}
	
	public BufferedImage[] getLayers() {
		return layers;
	}
	
	public String[] getLayerList() {
		if (layers == null || layers.length <= 0) {
			return new String[]{"0"};
		}
		String[] res = new String[layers.length];
		for (int i = 0; i < layers.length; i++) {
			res[i] = String.valueOf(i + 1);
		}
		return res;
	}

	public void updateLayer(int layer, BufferedImage image) throws ArrayIndexOutOfBoundsException {
		layers[layer].createGraphics().drawImage(image, 0, 0, null);
	}
	
	public void updateLayer(int layer, BufferedImage image, int offsetX, int offsetY) throws ArrayIndexOutOfBoundsException {
		layers[layer].createGraphics().drawImage(image, offsetX, offsetY, null);
	}
	
	public void clearLayer(int layer) {
		layers[layer] = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
		if (layer == 0) {
			Graphics g = layers[layer].createGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, pixelWidth, pixelHeight);
		}
	}
	
	public void clearLayer(int layer, int x, int y, int width, int height) {
		Graphics2D g = layers[layer].createGraphics();
		if(layer == 0) {
			g.setColor(Color.BLACK);
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		}
		g.fillRect(x, y, width, height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(!real) {
			return;
		}
		for (BufferedImage layer : layers) {
			g.drawImage(layer, 0, 0, null);
		}
		
		for (int i = gridSize; i < pixelWidth; i += gridSize) {
			g.setColor(Color.GRAY);
			g.drawLine(i, 0, i, pixelHeight);
		}
		
		for (int i = gridSize; i < pixelHeight; i += gridSize) {
			g.setColor(Color.GRAY);
			g.drawLine(0, i, pixelWidth, i);
		}
		g.setColor(new Color(0xFF, 0x00, 0x00, 0x7F));
		for (int x = 0; x < gridWidth; x ++) {
			for (int y = 0; y < gridHeight; y ++) {
				if (walkable[x][y]) {
					g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
				}
			}
		}
		g.setColor(Color.YELLOW);
		g.drawRect(selectedTileX * gridSize, selectedTileY * gridSize, gridSize, gridSize);
	}

	public BufferedImage getLayer(int i) {
		return layers[i];
	}
	
	public void setActiveLayer(int layer) {
		activeLayer = layer;
	}
	
	public Area getArea() {
		return new Area(layers, gridSize, walkable, actionTiles.toArray(new Tile[actionTiles.size()]));
	}
	
	public Tile getActionTileAt(int x, int y) {
		for (int i = 0; i < actionTiles.size(); i++) {
			Tile t = actionTiles.get(i);
			if (t.getX() == x && t.getY() == y) {
				return t;
			}
		}
		return null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!real) {
			return;
		}
		selectedTileX = (int)(e.getX() / gridSize);
		selectedTileY = (int)(e.getY() / gridSize);
		if (tileset) {
			Variables.selection = layers[0].getSubimage(selectedTileX * gridSize, selectedTileY * gridSize, gridSize, gridSize);
		} else {
			if (SwingUtilities.isMiddleMouseButton(e) || isShiftPressed) {
				settingTo = (walkable[selectedTileX][selectedTileY] ^= true);
				repaint();
				return;
			}
			if (isControlPressed) {
				
			}
			clearLayer(activeLayer, selectedTileX * gridSize, selectedTileY * gridSize, gridSize, gridSize);
			if (SwingUtilities.isLeftMouseButton(e)) {
				updateLayer(activeLayer, Variables.selection, selectedTileX * gridSize, selectedTileY * gridSize);
			}
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!real) {
			return;
		}
		selectedTileX = (int)(e.getX() / gridSize);
		selectedTileY = (int)(e.getY() / gridSize);
		if (tileset) {
			Variables.selection = layers[0].getSubimage(selectedTileX * gridSize, selectedTileY * gridSize, gridSize, gridSize);
		} else {
			if (SwingUtilities.isMiddleMouseButton(e) || isShiftPressed) {
				walkable[selectedTileX][selectedTileY] = settingTo;
				repaint();
				return;
			}
			clearLayer(activeLayer, selectedTileX * gridSize, selectedTileY * gridSize, gridSize, gridSize);
			if (SwingUtilities.isLeftMouseButton(e)) {
				updateLayer(activeLayer, Variables.selection, selectedTileX * gridSize, selectedTileY * gridSize);
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		if (e.getKeyCode() == 16) {
			isShiftPressed = true;
		}
		if (e.getKeyCode() == 17) {
			isControlPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 16) {
			isShiftPressed = false;
		}
		if (e.getKeyCode() == 17) {
			isControlPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}

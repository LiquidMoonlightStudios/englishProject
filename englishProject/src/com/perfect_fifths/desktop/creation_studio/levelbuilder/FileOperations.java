package com.perfect_fifths.desktop.levelbuilder;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.stream.*;
import javax.swing.JOptionPane;

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
		scanner.close();
		for (int i = 0; i < layers.length; i++) {
			layers[i] = ImageIO.read(new File(file.getParentFile(), "/" + getFileName(file) + i + ".pal"));
		}
		res.layers = layers;
		res.calcSize();
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
	
	public static boolean save(BufferedImage[] layers, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		//int[] intHeader = new int[1 + layers.length];
		//intHeader[0] = layers.length;
		byte[][] binaryLayers = new byte[layers.length][];
		for (int i = 0; i < layers.length; i++) {
			binaryLayers[i] = bufferedImageToByteArray(layers[i]);
			//intHeader[i + 1] = binaryLayers[i].length;
		}
		//os.write(intArrayToByteArray(intHeader));
		
		for (byte[] binLayer : binaryLayers) {
			os.write(binLayer);
		}
		os.flush();
		os.close();
		return true;
	}
	
	public static BufferedImage[] open(File file) throws IOException {
		byte[] data = Files.readAllBytes(Paths.get(file.toURI()));
		BufferedImage[] res;
		ArrayList<Integer> sof = new ArrayList<>();
		ArrayList<Integer> eof = new ArrayList<>();
		ArrayList<Byte[]> layers = new ArrayList<>();
		
		String dataString = new String(data);
		String startString = new String(new byte[] {(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47,
													(byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A});
		int index = dataString.indexOf(startString);
		System.out.println("Starts:");
		while (index >= 0) {
		    sof.add(index);
		    System.out.println("Index" + index);
		    index = dataString.indexOf(startString, index + 1);
		}
		System.out.println("Ends:");
		index = dataString.indexOf("IEND") + 7;
		while (index >= 7) {
		    eof.add(index);
		    System.out.println("Index" + index);
		    index = dataString.indexOf("END", index + 1) + 7;
		}
		
		for (int i = 0; i < sof.size(); i++) {
			layers.add(convertByteArray(dataString.substring(sof.get(i), eof.get(i)).getBytes()));
			System.out.println("Length" + dataString.substring(sof.get(i), eof.get(i)).getBytes().length);
		}
		res = new BufferedImage[layers.size()];
	    for (int i = 0; i < layers.size(); i++) {
	    	System.out.println(layers.get(i).length);
	    	res[i] = byteArrayToBufferedImage(data);
	    	System.out.println(res[i]);
	    }
	    return flip(res);
	}
	
	public static <T> T[] flip(T[] array) {
		T[] res = array;
		for (int i = 0; i < array.length; i++) {
			res[i] = array[(array.length - i) - 1];
		}
		return res;
	}
	
	public static Byte[] convertByteArray(byte[] data) {
		Byte[] res = new Byte[data.length];
		for (int i = 0; i < data.length; i++) {
			res[i] = data[i];
		}
		return res;
	}
	
	public static byte[] convertByteArray(Byte[] data) {
		byte[] res = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			res[i] = data[i];
		}
		return res;
	}
	
	public static byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image,"png",out);
		return out.toByteArray();
	}
	
	public static BufferedImage byteArrayToBufferedImage(byte[] data) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		return ImageIO.read(in);
	}
	
	public static int byteArrayToInt(byte[] val) {
		return ByteBuffer.wrap(val).getInt();
	}
	
	public static int[] byteArrayToIntArray(byte[] val) {
		int[] res = new int[val.length/4];
		for (int i = 0; i < res.length; i++) {
			res[i] = byteArrayToInt(Arrays.copyOfRange(val, i * 4, i * 4 + 4));
		}
		return res;
	}
	
	public static byte[] intArrayToByteArray(int[] val) {
		ByteBuffer byteBuf = ByteBuffer.allocate(val.length * 4).order(ByteOrder.BIG_ENDIAN);
        byteBuf.asIntBuffer().put(val);
        return byteBuf.array();
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

package com.perfect_fifths.asset_classes;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundTile extends Tile implements java.io.Serializable{

	Clip sound;
	
	public SoundTile(int x, int y, Clip sound) {
		super(x, y);
		this.sound = sound;
	}
	
	public SoundTile(int x, int y, File sound) {
		super(x, y);
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(sound.getAbsoluteFile());
			this.sound = AudioSystem.getClip();
	        this.sound.open(audioInputStream);
	        audioInputStream.close();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
        
	}

	@Override
	public void doAction() {
		sound.start();
	}

	@Override
	public String getType() {
		return "Sound";
	}
}

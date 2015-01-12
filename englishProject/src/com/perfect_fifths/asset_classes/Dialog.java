package com.perfect_fifths.asset_classes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Dialog implements java.io.Serializable {

	transient BufferedImage leftImage, rightImage;
	SoundText[] dialog;

	public BufferedImage getLeftImage() {
		return leftImage;
	}

	public BufferedImage getRightImage() {
		return rightImage;
	}

	public SoundText[] getDialog() {
		return dialog;
	}

	public Dialog(BufferedImage leftImage, BufferedImage rightImage,
			SoundText[] dialog) {
		this.leftImage = leftImage;
		this.rightImage = rightImage;
		this.dialog = dialog;
	}

	public class SoundText {
		Clip sound;
		String text;

		public SoundText(Clip sound, String text) {
			this.sound = sound;
			this.text = text;
		}

		public SoundText(File sound, String text) {
			AudioInputStream audioInputStream;
			try {
				audioInputStream = AudioSystem.getAudioInputStream(sound
						.getAbsoluteFile());
				this.sound = AudioSystem.getClip();
				this.sound.open(audioInputStream);
				audioInputStream.close();
			} catch (UnsupportedAudioFileException | IOException
					| LineUnavailableException e) {
				e.printStackTrace();
			}
			this.text = text;
		}

		public Clip getSound() {
			return sound;
		}

		public void setSound(Clip sound) {
			this.sound = sound;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void playSound() {
			sound.start();
		}
	}
}

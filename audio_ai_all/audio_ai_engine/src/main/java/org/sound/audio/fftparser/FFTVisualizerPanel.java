package org.sound.audio.fftparser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.sound.audio.fft.Complex;

class FFTVisualizerPanel extends JPanel {
	 
	private static final int PIXEL_SIZE = 2;
	
	private Complex[][] fft;
	private boolean logModeEnabled;

	public FFTVisualizerPanel(Complex[][] fft, boolean logModeEnabled) {
		this.fft = fft;
		this.logModeEnabled = logModeEnabled;

	}

	public FFTVisualizerPanel(Complex[][] fft) {
		this(fft, true);
	}

	private void visualize(Graphics g2d) {
		int blockSizeX = PIXEL_SIZE;
		int blockSizeY = PIXEL_SIZE;

		int frameCount = fft.length;
		for (int frame = 0; frame < frameCount; frame++) {
			int frameWindowSize = fft[frame].length;
			int freq = 0;
			for (int frSample = 0; frSample < frameWindowSize; frSample++) {
				// To get the magnitude of the sound at a given frequency slice
				// get the abs() from the complex number.
				// In this case I use Math.log to get a more managable number
				// (used for color)
				double magnitude = Math.log(fft[frame][freq].abs() + 1);
				// The more blue in the color the more intensity for a given
				// frequency point:
				g2d.setColor(new Color(0, (int) magnitude * 10,
						(int) magnitude * 20));
				// Fill:
				g2d.fillRect(frame * blockSizeX,
						(frameWindowSize - frSample) * blockSizeY, blockSizeX,
						blockSizeY);

				// I used a improviced logarithmic scale and normal scale:
				if (this.logModeEnabled
						&& (Math.log10(frSample) * Math.log10(frSample)) > 1) {
					freq += (int) (Math.log10(frSample) * Math.log10(frSample));
				} else {
					freq++;
				}
			}
		}
		setSize(fft[0].length * blockSizeX, fft.length * blockSizeY);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		visualize((Graphics2D) g);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		visualize((Graphics2D) g);
	}
}

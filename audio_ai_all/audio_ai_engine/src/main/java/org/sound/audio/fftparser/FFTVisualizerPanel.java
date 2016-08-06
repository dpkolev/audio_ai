package org.sound.audio.fftparser;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.sound.audio.fft.Complex;

public class FFTVisualizerPanel {

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

	public void visualize() {
		int blockSizeX = PIXEL_SIZE;
		int blockSizeY = PIXEL_SIZE;
		int frameCount = fft.length;
		int frameWindowSize = fft[0].length;
		// BufferedImage outImage = new
		// BufferedImage(frameWindowSize*blockSizeX, frameCount*blockSizeY,
		// BufferedImage.TYPE_INT_RGB);
		BufferedImage outImage = new BufferedImage(frameCount, calculateImageHeight(frameWindowSize, this.logModeEnabled),
				BufferedImage.TYPE_INT_RGB);
		System.out.println(String.format("Size will be %d to %d picture", outImage.getWidth(), outImage.getHeight()));
		for (int frame = 0; frame < frameCount; frame++) {
			int currHeight = outImage.getHeight();
			for (int frSample = 0; frSample < frameWindowSize;) {
				
				double preMagnitude = fft[frame][frSample].abs();
				double magnitude = Math.log(preMagnitude + 1); /// Math.log(2);
				// The more blue in the color the more intensity for a given
				// frequency point:
				outImage.setRGB(frame, --currHeight, new Color(0, (int) (magnitude * 10),
								(int) (magnitude * 20)).getRGB());
				// g2d.setColor(new Color(0, (int) magnitude * 10,
				// (int) magnitude * 20));
				// // Fill:
				// g2d.fillRect(frame * blockSizeX,
				// (frameWindowSize - frSample) * blockSizeY, blockSizeX,
				// blockSizeY);

				// I used a improviced logarithmic scale and normal scale:
				frSample += getNextOffset(frSample, this.logModeEnabled);
				
			}
		}
		try {
			ImageIO.write(outImage, "jpeg",
					new File("Freq_" + new Date().getTime() + ".jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new PopUpJFrame(outImage, "Frequencies");
	}
	
	private int getNextOffset(int current, boolean isLogScale) {
		int offset = (int)Math.log10(current + 1);
		if (isLogScale && offset > 0) {
			return offset*offset;
		} else {
			return 1;
		}
	}
	
	private int calculateImageHeight(int frameWindowSize, boolean isLogScale) {
		if (!isLogScale) {
			return frameWindowSize;
		} else {
			int height = 0;
			int accumulator = 0;
			while (accumulator < frameWindowSize) {
				height++;
				accumulator += getNextOffset(accumulator, true);
			}
			return height;
		}
	}
}

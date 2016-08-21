package org.sound.audio.fftparser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.sound.audio.fft.Complex;
import org.sound.audio.grouping.BucketDiscretization;

public class FFTVisualizerPanel {

	private static final double SPECTROGRAM_THRESHOLD = 0.65*255;

	private static final int PIXEL_SIZE = 2;

	private FFTAudioInfo fftAudioInfo;
	
	private double[][] binnedMagnitudeScaledFFT;
	
	private int lastBucketCount;
	
	private boolean isLogScalePictureGeneration;

	public FFTVisualizerPanel(Complex[][] fft, boolean isLogScalePictureGeneration) {
		this.fftAudioInfo = new FFTAudioInfo(fft);
		this.isLogScalePictureGeneration = isLogScalePictureGeneration;

	}

	public FFTVisualizerPanel(Complex[][] fft) {
		this(fft, true);
	}

	public void visualize() {
		int blockSizeX = PIXEL_SIZE;
		int blockSizeY = PIXEL_SIZE;
		double[][] magnitudeMap = fftAudioInfo.getMagnitudeMap();
		int frameCount = magnitudeMap.length;
		int frameWindowSize = magnitudeMap[0].length;
		BufferedImage outImage = 
				new BufferedImage(frameCount*PIXEL_SIZE, calcImageHeight(frameWindowSize, this.isLogScalePictureGeneration)*PIXEL_SIZE, BufferedImage.TYPE_INT_RGB);
		System.out.println(String.format("Size will be %d to %d px", outImage.getWidth(), outImage.getHeight()));
		Graphics g = outImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, outImage.getWidth(), outImage.getHeight());
//		double maxFFTMagn = 0;
//		for (Complex[] dArray : fft) {
//			for (Complex d : dArray) {
//				//double magn = Math.log10(d.abs() + 1);
//				double magn = 10*Math.log10(Math.pow(d.abs(), 2)+1);
//				if (maxFFTMagn < magn) {
//					maxFFTMagn = magn;
//				}
//			}
//		}
//		double colorScaler = 255/maxFFTMagn;
		for (int frame = 0; frame < frameCount; frame++) {
			int currHeight = outImage.getHeight()-1;
			for (int frSample = 0; frSample < frameWindowSize;) {
				//double magnitude = Math.log(fft[frame][frSample].abs() + 1);
				double magnitude = 10*Math.log10(Math.pow(magnitudeMap[frame][frSample], 2)+1);
				int rgbPart = (int) (magnitude * 2);
				rgbPart = rgbPart < 256 ? rgbPart : 255; 
				rgbPart = 255 - rgbPart;
				int rgb = new Color(rgbPart, rgbPart, rgbPart).getRGB();
				for (int x = 0; x < blockSizeX; x++) {
					for (int y = 0; y < blockSizeY; y++) {
						outImage.setRGB(frame*blockSizeX + x, currHeight - y, rgb);
					}
				}
				currHeight -= blockSizeY;
//				outImage.setRGB(frame, --currHeight, new Color((int) (magnitude * 15), (int) (magnitude * 15),
//						(int) (magnitude * 15)).getRGB());
				// Improviced logarithmic scale and normal scale:
				frSample += getNextOffset(frSample, this.isLogScalePictureGeneration);
			}
		}
		try {
			ImageIO.write(outImage, "jpeg",
					new File("Freq_" + new Date().getTime() + ".jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//new PopUpJFrame(outImage, "Frequencies");
	}
	
	
	public void visualizeScaled(int bucketCount, boolean isLogColorScale) {
		int blockSizeX = PIXEL_SIZE;
		int blockSizeY = PIXEL_SIZE;
		double[][] magnitudeRefFFT = getMagnitudeScaledFFT(bucketCount);
		double maxFFT = 0;
		for (double[] dArray : magnitudeRefFFT) {
			for (double d : dArray) {
				if (maxFFT < d) {maxFFT = d;}
			}
		}
		int frameCount = magnitudeRefFFT.length;
		int frameWindowSize = magnitudeRefFFT[0].length;
		BufferedImage outImage = new BufferedImage(frameCount*blockSizeX, frameWindowSize*blockSizeY,
				BufferedImage.TYPE_INT_RGB);
		System.out.println(String.format("Size will be %d to %d picture", outImage.getWidth(), outImage.getHeight()));
		Graphics g = outImage.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, outImage.getWidth(), outImage.getHeight());
		double colorScaler = (255/maxFFT);
		if (isLogColorScale) {
			colorScaler = 255/log2(maxFFT+1);
		}
		System.out.println("Selected color scaler is " + colorScaler);
		for (int frame = 0; frame < frameCount; frame++) {
			int currHeight = outImage.getHeight() - 1;
			for (int frSample = 0; frSample < frameWindowSize; frSample++) {
				int color = 0;
				if (isLogColorScale) {
					color = (int)(((int)log2(magnitudeRefFFT[frame][frSample]+1)*colorScaler));
				} else {
					color = (int)(magnitudeRefFFT[frame][frSample]*colorScaler);
				}
				//if (color < SPECTROGRAM_THRESHOLD) {color = 0;}
				int rgb = new Color(color, color, color).getRGB();
				for (int x = 0; x < blockSizeX; x++) {
					for (int y = 0; y < blockSizeY; y++) {
						outImage.setRGB(frame*blockSizeX + x, currHeight - frSample*blockSizeY - y, rgb);
					}
				}
//				outImage.setRGB(frame, --currHeight, new Color((int) (magnitude * 15), (int) (magnitude * 15),
//						(int) (magnitude * 15)).getRGB());
			}
		}
		try {
			ImageIO.write(outImage, "jpeg",
					new File("Freq_Scaled" + new Date().getTime() + ".jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//new PopUpJFrame(outImage, "Frequencies");
	}
	
	
	
	public void visualizeFrame(int frame, int bucketCount, boolean logScale) {
		int blockSizeX = PIXEL_SIZE;
		double[][] magnitudeRefFFT = getMagnitudeScaledFFT(bucketCount);
		double maxFFT = 0;
		for (double[] dArray : magnitudeRefFFT) {
			for (double d : dArray) {
				if (maxFFT < d) {maxFFT = d;}
			}
		}
		double scaler = 1;
		int imageHeight = 1000;
		if (logScale) {
			imageHeight = 1 + (int)Math.ceil(Math.log(maxFFT+1));
		} else {
			scaler = (imageHeight-1)/maxFFT;
		}
		if (frame > magnitudeRefFFT.length) {
			System.err.println("Frame is out of scope - getting middle frame instead !");
			frame = magnitudeRefFFT.length/2;
		}
		int frameWindowSize = magnitudeRefFFT[frame].length;
		
		BufferedImage outImage = new BufferedImage(frameWindowSize*blockSizeX, imageHeight, BufferedImage.TYPE_INT_RGB);
		System.out.println(String.format("Size will be %d to %d picture", outImage.getWidth(), outImage.getHeight()));
		Graphics g = outImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, outImage.getWidth(), outImage.getHeight());
		int currentHeight = imageHeight - 1;
		for (int frSample = 0; frSample < frameWindowSize; frSample++) {
			int rgb = Color.BLACK.getRGB();
			int freqMagnitude = 0;
			if (logScale) {
				freqMagnitude = (int)Math.log(magnitudeRefFFT[frame][frSample] + 1);
			} else {
				freqMagnitude = (int)(scaler * magnitudeRefFFT[frame][frSample]);
			}
			System.out.println("Freq magnitude is " + freqMagnitude);
			System.out.println("Height is " + imageHeight);
			System.out.println("Current Height is " + currentHeight);
			for (int x = 0; x < blockSizeX; x++) {
				for (int y = 0; y < freqMagnitude; y++) {
					try {
						outImage.setRGB(frSample * blockSizeX + x, currentHeight - y, rgb);
					} catch (Throwable t) {
						System.out.println((frSample * blockSizeX + x)  + " - " + (currentHeight - y));
						throw t;
					}
				}
			}
			// outImage.setRGB(frame, --currHeight, new Color((int) (magnitude *
			// 15), (int) (magnitude * 15),
			// (int) (magnitude * 15)).getRGB());
		}
		try {
			ImageIO.write(outImage, "jpeg",
					new File("Freq_Scaled_Frame" + frame + "_"+ new Date().getTime() + ".jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new PopUpJFrame(outImage, "Frequencies_Frame" + frame);
	}
	
	private int getNextOffset(int current, boolean isLogScale) {
		int offset = (int)Math.log10(current + 1);
		if (isLogScale && offset > 0) {
			return offset*offset;
		} else {
			return 1;
		}
	}
	
	private int calcImageHeight(int frameWindowSize, boolean isLogScale) {
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
	
	private double log2(double d) {
		return Math.log(d)/Math.log(2);
	}

	public double[][] getMagnitudeScaledFFT(int bucketCount) {
		if (binnedMagnitudeScaledFFT == null || lastBucketCount != bucketCount) {
		    binnedMagnitudeScaledFFT = BucketDiscretization.discretizeByMean(fftAudioInfo.getMagnitudeMap(), bucketCount);
		}
		return binnedMagnitudeScaledFFT;
	}
}

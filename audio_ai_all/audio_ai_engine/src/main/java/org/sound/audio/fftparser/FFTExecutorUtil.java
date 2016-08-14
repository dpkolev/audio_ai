package org.sound.audio.fftparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.sound.audio.Channel;
import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fft.FFT;
import org.sound.audio.fft.FFTColUtil;

public class FFTExecutorUtil {
	
	public static final Complex[][] transform(RawFilePlayer player) {
		if (!player.isPlayed()) {
			player.playback();
		}
		Channel ch = player.getLeftChannel();
		int frameSize = ch.getFrameSize();
		List<Byte[]> channelData = ch.getChannelData();
		Complex[][] results = new Complex[channelData.size()][];
		for (int frameNumber = 0; frameNumber < channelData.size(); frameNumber++) {
			Byte[] frame = channelData.get(frameNumber);
			Complex[] frameForFFT = new Complex[frameSize];
			for (int i = 0; i < frameSize; i++) {
				try {
					frameForFFT[i] = new Complex(frame[i], 0);
				} catch (Throwable e) {
					System.out.println("For i = " + i);
					throw e;
				}
			}
			results[frameNumber] = FFT.fft(frameForFFT);
		}
		return results;
	}

	public static final Complex[][] transformToRealFFT(RawFilePlayer player) {
		if (!player.isPlayed()) {
			player.playback();
		}
		Channel ch = player.getLeftChannel();
		int frameSize = ch.getFrameSize();
		List<Byte[]> channelData = ch.getChannelData();
		Complex[][] results = new Complex[channelData.size()][];
		for (int frameNumber = 0; frameNumber < channelData.size(); frameNumber++) {
			Byte[] frame = channelData.get(frameNumber);
			Complex[] frameForFFT = new Complex[frameSize];
			for (int i = 0; i < frameSize; i++) {
				try {
					frameForFFT[i] = new Complex(frame[i], 0);
				} catch (Throwable e) {
					System.out.println("For i = " + i);
					throw e;
				}
			}
			Complex[] fftFrame = FFT.fft(frameForFFT);
			results[frameNumber] = new Complex[fftFrame.length/4];
			System.arraycopy(fftFrame, 0, results[frameNumber], 0, results[frameNumber].length);
		}
		return results;
	}
	
	
	public static final void printFFT (Complex[][] toPrint, String fileName) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		for (Complex[] line : toPrint) {
			pw.println(Arrays.toString(line));
		}
		pw.close();
	}
	
	// Columbia addition:
	public static final Pair[] transformFFTColumbiaVersion(RawFilePlayer player) {
		if (!player.isPlayed()) {
			player.playback();
		}
		Channel ch = player.getLeftChannel();
		int frameSize = ch.getFrameSize();
		List<Byte[]> channelData = ch.getChannelData();
		Pair[] results = new Pair[channelData.size()];
		FFTColUtil.init(frameSize);
		for (int frameNumber = 0; frameNumber < channelData.size(); frameNumber++) {
			double[] real = new double[frameSize];
			double[] img = new double[frameSize];
			Byte[] frame = channelData.get(frameNumber);
			for (int i = 0; i < frameSize; i++) {
				real[i] = frame[i];
				img[i] = 0;
			}
			FFTColUtil.fft(real, img);
			results[frameNumber] = new Pair(real, img);
		}
		return results;
	}
	
	
	public static final Pair[] transformToRealOnlyColumbia(RawFilePlayer player) {
		if (!player.isPlayed()) {
			player.playback();
		}
		Channel ch = player.getLeftChannel();
		int frameSize = ch.getFrameSize();
		List<Byte[]> channelData = ch.getChannelData();
		Pair[] results = new Pair[channelData.size()];
		for (int frameNumber = 0; frameNumber < channelData.size(); frameNumber++) {
			double[] real = new double[frameSize];
			double[] img = new double[frameSize];
			Byte[] frame = channelData.get(frameNumber);
			for (int i = 0; i < frameSize; i++) {
				real[i] = frame[i];
				img[i] = 0;
			}
			FFTColUtil.fft(real, img);
			double[] realCut = new double[frameSize/4];
			System.arraycopy(real, 0, realCut, 0, realCut.length);
			double[] imgCut = new double[frameSize/4];
			System.arraycopy(img, 0, imgCut, 0, imgCut.length);
			results[frameNumber] = new Pair(realCut, imgCut);
		}
		return results;
	}
	
	
	public static final double[][] transformToMagnitudeColumbia(RawFilePlayer player) {
		if (!player.isPlayed()) {
			player.playback();
		}
		Channel ch = player.getLeftChannel();
		int frameSize = ch.getFrameSize();
		List<Byte[]> channelData = ch.getChannelData();
		double[][] results = new double[channelData.size()][];
		for (int frameNumber = 0; frameNumber < channelData.size(); frameNumber++) {
			double[] real = new double[frameSize];
			double[] img = new double[frameSize];
			Byte[] frame = channelData.get(frameNumber);
			for (int i = 0; i < frameSize; i++) {
				real[i] = frame[i];
				img[i] = 0;
			}
			FFTColUtil.fft(real, img);
			double[] realCut = new double[frameSize/4];
			System.arraycopy(real, 0, realCut, 0, realCut.length);
			double[] imgCut = new double[frameSize/4];
			System.arraycopy(img, 0, imgCut, 0, imgCut.length);
			results[frameNumber] = FFTColUtil.toMagnitude(realCut, imgCut);
		}
		return results;
	}
	
	
	public static class Pair {
		public double[] real;
		public double[] img;
		public Pair(double[] real, double[] img) {
			this.real = real;
			this.img = img;
		}
		
		public double[] toMagnitude() {
			return FFTColUtil.toMagnitude(real, img);
		}
	}
	
}

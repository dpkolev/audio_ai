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

public class FFTExecutor {
	
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
	
	
	public static final Complex[][] transformToRealOnly(RawFilePlayer player) {
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
	
}

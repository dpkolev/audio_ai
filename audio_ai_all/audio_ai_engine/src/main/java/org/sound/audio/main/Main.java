package org.sound.audio.main;

import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fftparser.FFTExecutor;
import org.sound.audio.fftparser.FFTVisualizerFrame;

public class Main {
	public static void main(String[] args) {

		RawFilePlayer player = new RawFilePlayer();
		Complex[][] fft = FFTExecutor.transform(player);
		// FFTExecutor.printFFT(fft, "fft.txt");

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FFTVisualizerFrame(fft, false);
			}
		});
	}
}

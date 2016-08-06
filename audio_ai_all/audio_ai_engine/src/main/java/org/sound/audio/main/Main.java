package org.sound.audio.main;

import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fftparser.FFTExecutor;
import org.sound.audio.fftparser.FFTVisualizer;

public class Main {
	public static void main(String[] args) {

		RawFilePlayer player = new RawFilePlayer();
		Complex[][] fft = FFTExecutor.transform(player);
		//FFTExecutor.printFFT(fft, "fft.txt");
		new FFTVisualizer(fft, false);
	}
}

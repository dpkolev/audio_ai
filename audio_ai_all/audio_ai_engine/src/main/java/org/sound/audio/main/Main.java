package org.sound.audio.main;

import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fftparser.FFTExecutor;
import org.sound.audio.fftparser.FFTVisualizerPanel;

public class Main {
	public static void main(String[] args) {

		RawFilePlayer player = new RawFilePlayer("test_Aphex_Twin-Window_Licker.mp3");//"test2_Mark_Petrie_Virtus.mp3");//
		Complex[][] fft = FFTExecutor.transform(player);
		// FFTExecutor.printFFT(fft, "fft.txt");

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FFTVisualizerPanel(fft, true).visualize();
			}
		});
	}
}

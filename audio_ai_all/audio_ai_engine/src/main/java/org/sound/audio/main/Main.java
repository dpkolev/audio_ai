package org.sound.audio.main;

import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fftparser.FFTExecutor;
import org.sound.audio.fftparser.FFTVisualizerPanel;

public class Main {
	public static void main(String[] args) {

		RawFilePlayer player = new RawFilePlayer("test_Aphex_Twin-Window_Licker.mp3");//"test_Venetian_Snares_Look.mp3");//"test2_Mark_Petrie_Virtus.mp3");//
		//Complex[][] fft = FFTExecutor.transform(player);
		Complex[][] fft = FFTExecutor.transformToRealOnly(player);
		// FFTExecutor.printFFT(fft, "fft.txt");

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				FFTVisualizerPanel panel = new FFTVisualizerPanel(fft, true);
				panel.visualize();
				panel.visualizeScaled(false,512);
				//panel.visualizeFrame(56, 256, false);
			}
		});
	}
}

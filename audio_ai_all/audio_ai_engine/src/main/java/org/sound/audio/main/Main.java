package org.sound.audio.main;

import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fftparser.FFTExecutorUtil;
import org.sound.audio.fftparser.FFTVisualizerPanel;

public class Main {
	public static void main(String[] args) {

		RawFilePlayer player = new RawFilePlayer("test_300_Hz_Sine_Wave_Sound_Frequency_Tone.mp3");
		//"test_Aphex_Twin-Window_Licker.mp3");
		//"test_Venetian_Snares_Look.mp3");
		//"test_Scandroid_Pro-bots_Robophobes_feat_Circle_of_Dust.mp3");
		//"test2_Mark_Petrie_Virtus.mp3");
		//"Spectrogram_Aphex_Twin.mp3");
		//"test_300_Hz_Sine_Wave_Sound_Frequency_Tone.mp3");
		//Complex[][] fft = FFTExecutor.transform(player);
		Complex[][] fft = FFTExecutorUtil.transformToRealFFT(player);
		// FFTExecutor.printFFT(fft, "fft.txt");

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				FFTVisualizerPanel panel = new FFTVisualizerPanel(fft, false);
				panel.visualize();
				panel.visualizeScaled(512, false);
				panel.visualizeFrame(800, 512, false);
			}
		});
	}
}

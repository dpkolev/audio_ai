package org.sound.audio.main;

import java.util.Arrays;

import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fftparser.FFTAudioInfo;
import org.sound.audio.fftparser.FFTExecutorUtil;
import org.sound.audio.fftparser.FFTVisualizerPanel;
import org.sound.audio.grouping.Frequency;
import org.sound.audio.grouping.LinearFrequencyScale;
import org.sound.audio.hashextract.Hasher;
import org.sound.audio.hashextract.Hasher.HASHER_TYPE;
import org.sound.audio.hashextract.KeyPointsExtractor;
import org.sound.audio.samplingwindows.BlackmanWindow;
import org.sound.audio.samplingwindows.HammingWindow;
import org.sound.audio.samplingwindows.HanningWindow;
import org.sound.audio.samplingwindows.RectangularWindow;

public class Main {
	public static void main(String[] args) {

		RawFilePlayer player = new RawFilePlayer("test_Venetian_Snares_Look.mp3", HammingWindow.class);
		//"test_Aphex_Twin-Window_Licker.mp3");
		//"test_Venetian_Snares_Look.mp3");
		//"test_Scandroid_Pro-bots_Robophobes_feat_Circle_of_Dust.mp3");
		//"test2_Mark_Petrie_Virtus.mp3");
		//"Spectrogram_Aphex_Twin.mp3");
		//"test_300_Hz_Sine_Wave_Sound_Frequency_Tone.mp3");
		//Complex[][] fft = FFTExecutorUtil.transform(player);
		Complex[][] fft = FFTExecutorUtil.transformToRealFFT(player);
		// FFTExecutor.printFFT(fft, "fft.txt");

		FFTAudioInfo audioInfo = new FFTAudioInfo(fft);
		KeyPointsExtractor kpe = new KeyPointsExtractor(audioInfo, new LinearFrequencyScale(512,0,fft[0].length));
		Frequency[][] biggest = kpe.getBiggestPerBandBuckets();
		long[] hashes = Hasher.createHashes(biggest, HASHER_TYPE.LINEAR);
		System.out.println("Hashes count: " + hashes.length);
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				FFTVisualizerPanel panel = new FFTVisualizerPanel(fft, false);
				panel.visualize();
				panel.visualizeScaled(512, true);
				//panel.visualizeFrame(800, 512, false);
			}
		});
	}
}

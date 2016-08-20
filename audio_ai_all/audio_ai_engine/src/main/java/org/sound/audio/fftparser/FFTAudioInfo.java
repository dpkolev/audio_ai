package org.sound.audio.fftparser;

import org.sound.audio.RawFilePlayer;
import org.sound.audio.fft.Complex;
import org.sound.audio.fftparser.FFTExecutorUtil.Pair;
import org.sound.audio.grouping.Frequency;

public class FFTAudioInfo {

	private double[][] magnitudeMap;
	
	private Complex[][] fft;
	
	private Pair[] fftCol;

	public FFTAudioInfo(Complex[][] fft) {
		this.fft = fft;
		this.magnitudeMap = new double[fft.length][];
		for (int row = 0; row < fft.length; row++) {
			int rowSize = fft[row].length;
			magnitudeMap[row] = new double[rowSize];
			for (int possition = 0; possition < rowSize; possition++) {
				magnitudeMap[row][possition] = fft[row][possition].abs();
			}
		}
	}

	public FFTAudioInfo(RawFilePlayer player) {
		this(FFTExecutorUtil.transformToRealFFT(player));
	}

	public FFTAudioInfo(Pair[] columbiaFFTImpl) {
		this.fftCol = columbiaFFTImpl;
		this.magnitudeMap = new double[columbiaFFTImpl.length][];
		for (int row = 0; row < columbiaFFTImpl.length; row++) {
			magnitudeMap[row] = columbiaFFTImpl[row].toMagnitude();
		}
	}

	public FFTAudioInfo(double[][] magnitudes) {
		this.magnitudeMap = magnitudes;
	}

	public double[][] getMagnitudeMap() {
		return magnitudeMap;
	}
	
	public Complex[][] getFft() {
		return fft;
	}
	
	public Pair[] getFftCol() {
		return fftCol;
	}
}

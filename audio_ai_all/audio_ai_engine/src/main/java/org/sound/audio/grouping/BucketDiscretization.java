package org.sound.audio.grouping;

import java.util.HashMap;
import java.util.Map;

import org.sound.audio.fft.Complex;

public class BucketDiscretization {
	
	public static double[][] discretizeByBiggest(Complex[][] fftMatrix,
			int targetBucketSize, boolean logScale) {
		return discretize(fftMatrix, targetBucketSize, logScale, HEURISTIC.BIGGEST);
	}
	
	public static double[][] discretizeByMean(Complex[][] fftMatrix,
			int targetBucketSize, boolean logScale) {
		return discretize(fftMatrix, targetBucketSize, logScale, HEURISTIC.MEAN);
	}
	
	public static double[][] discretizeByMedianValue(Complex[][] fftMatrix,
			int targetBucketSize, boolean logScale) {
		return discretize(fftMatrix, targetBucketSize, logScale, HEURISTIC.MEDIAN);
	}

	private static double[][] discretize(Complex[][] fftMatrix,
			int targetBucketSize, boolean logScale, HEURISTIC heuristic) {
		int freqCount = fftMatrix[0].length;
		int scaleFactor = (int) Math.ceil(freqCount / targetBucketSize);
		double[][] scaledFFT = new double[fftMatrix.length][];
		for (int scaledFreqWindow = 0; scaledFreqWindow < scaledFFT.length; scaledFreqWindow++) {
			Complex[] fftRow = fftMatrix[scaledFreqWindow];
			scaledFFT[scaledFreqWindow] = calculate(fftRow, scaleFactor,
					targetBucketSize, logScale, heuristic);
		}
		return scaledFFT;
	}

	private static double[] calculate(Complex[] fftRow, int scaleFactor,
			int targetBucketSize, boolean logScale, HEURISTIC heuristic) {
		double[] result = new double[targetBucketSize];
		for (int bucket = 0; bucket < targetBucketSize; bucket++) {
			result[bucket] = calculate(fftRow, bucket * scaleFactor,
					(bucket + 1) * scaleFactor, heuristic);
		}
		return result;
	}

	private static double calculate(Complex[] fftRow, int start, int end,
			HEURISTIC heuristic) {
		return heuristicImpl.get(heuristic).heuristicCalculation(fftRow, start, (end < fftRow.length) ? end : fftRow.length);
	}

	//HERE BE DRAGONS ...
	
	public static interface HeuristicCalculation {
		double heuristicCalculation(Complex[] data, int start, int end);
	}

	public static enum HEURISTIC {
		BIGGEST, MEDIAN, MEAN
	}
	
	private static final Map<HEURISTIC, HeuristicCalculation> heuristicImpl = new HashMap<HEURISTIC, HeuristicCalculation>();

	static {
		heuristicImpl.put(HEURISTIC.BIGGEST, new HeuristicCalculation() {

			@Override
			public double heuristicCalculation(Complex[] data, int start,
					int end) {
				double biggest = data[start].abs();
				for (int i = start + 1; i < end; i++) {
					double current = data[i].abs();
					if (biggest < current) {
						biggest = current;
					}
				}
				return biggest;
			}
		});

		heuristicImpl.put(HEURISTIC.MEAN, new HeuristicCalculation() {

			@Override
			public double heuristicCalculation(Complex[] data, int start, int end) {
				double sum = 0.0;
				for (int i = start; i < end; i++) {
					sum += data[i].abs();
				}
				return sum/(end-start);
			}
		});
		
		heuristicImpl.put(HEURISTIC.MEDIAN, new HeuristicCalculation() {

			@Override
			public double heuristicCalculation(Complex[] data, int start, int end) {
				return data[(start+end)/2].abs();
			}
		});
	}
}

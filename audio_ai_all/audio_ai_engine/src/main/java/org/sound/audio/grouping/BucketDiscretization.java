package org.sound.audio.grouping;

import org.sound.audio.fft.Complex;
import static org.sound.audio.grouping.Heuristics.*;

public class BucketDiscretization {

	public static double[][] discretizeByBiggest(Complex[][] fftMatrix,
			int targetBucketSize) {
		return discretize(fftMatrix, targetBucketSize, HEURISTIC.BIGGEST);
	}

	public static double[][] discretizeByMean(Complex[][] fftMatrix,
			int targetBucketSize) {
		return discretize(fftMatrix, targetBucketSize, HEURISTIC.MEAN);
	}

	public static double[][] discretizeByMedianValue(Complex[][] fftMatrix,
			int targetBucketSize) {
		return discretize(fftMatrix, targetBucketSize, HEURISTIC.MEDIAN);
	}

	private static double[][] discretize(Complex[][] fftMatrix,
			int targetBucketSize, HEURISTIC heuristic) {
		int freqCount = fftMatrix[0].length;
		int scaleFactor = (int) Math.ceil(freqCount / targetBucketSize);
		double[][] scaledFFT = new double[fftMatrix.length][];
		for (int scaledFreqWindow = 0; scaledFreqWindow < scaledFFT.length; scaledFreqWindow++) {
			Complex[] fftRow = fftMatrix[scaledFreqWindow];
			scaledFFT[scaledFreqWindow] = calculate(fftRow, scaleFactor,
					targetBucketSize, heuristic);
		}
		return scaledFFT;
	}

	private static double[] calculate(Complex[] fftRow, int scaleFactor,
			int targetBucketSize, HEURISTIC heuristic) {
		double[] result = new double[targetBucketSize];
		for (int bucket = 0; bucket < targetBucketSize; bucket++) {
			result[bucket] = calculateSingle(fftRow, bucket * scaleFactor,
					(bucket + 1) * scaleFactor, heuristic);
		}
		return result;
	}

	private static double calculateSingle(Complex[] fftRow, int start, int end,
			HEURISTIC heuristic) {
		return HEURISTICS_IMPL.get(heuristic).heuristicCalculation(fftRow, start,
				(end < fftRow.length) ? end : fftRow.length);
	}
}

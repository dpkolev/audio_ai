package org.sound.audio.grouping;

import java.util.HashMap;
import java.util.Map;

import org.sound.audio.fft.Complex;

public class Heuristics {
	
	public static interface HeuristicCalculation {
		double heuristicCalculation(Complex[] data, int start, int end);
		double heuristicCalculation(double[] data, int start, int end);
		int heuristicCalculationIndex(Complex[] data, int start, int end);
		int heuristicCalculationIndex(double[] data, int start, int end);
	}

	public static enum HEURISTIC {
		BIGGEST, MEDIAN, MEAN
	}

	public static final Map<HEURISTIC, HeuristicCalculation> HEURISTICS_IMPL = new HashMap<HEURISTIC, HeuristicCalculation>();

	static {
		HEURISTICS_IMPL.put(HEURISTIC.BIGGEST, new HeuristicCalculation() {

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

			@Override
			public double heuristicCalculation(double[] data, int start,
					int end) {
				double biggest = data[start];
				for (int i = start + 1; i < end; i++) {
					if (biggest < data[i]) {
						biggest = data[i];
					}
				}
				return biggest;
			}

			@Override
			public int heuristicCalculationIndex(Complex[] data, int start,
					int end) {
				int indexBiggest = start;
				double biggest = data[start].abs();
				for (int i = start + 1; i < end; i++) {
					double current = data[i].abs();
					if (biggest < current) {
						biggest = current;
						indexBiggest = i;
					}
				}
				return indexBiggest;
			}

			@Override
			public int heuristicCalculationIndex(double[] data, int start,
					int end) {
				int indexBiggest = start;
				for (int i = start + 1; i < end; i++) {
					if (data[indexBiggest] < data[i]) {
						indexBiggest = i;
					}
				}
				return indexBiggest;
			}
		});

		HEURISTICS_IMPL.put(HEURISTIC.MEAN, new HeuristicCalculation() {

			@Override
			public double heuristicCalculation(Complex[] data, int start,
					int end) {
				double sum = 0.0;
				for (int i = start; i < end; i++) {
					sum += data[i].abs();
				}
				return sum / (end - start);
			}

			@Override
			public double heuristicCalculation(double[] data, int start,
					int end) {
				double sum = 0.0;
				for (int i = start; i < end; i++) {
					sum += data[i];
				}
				return sum / (end - start);
			}

			@Override
			public int heuristicCalculationIndex(Complex[] data, int start,
					int end) {
				return (start + end) / 2;
			}

			@Override
			public int heuristicCalculationIndex(double[] data, int start,
					int end) {
				return (start + end) / 2;
			}
		});

		HEURISTICS_IMPL.put(HEURISTIC.MEDIAN, new HeuristicCalculation() {

			@Override
			public double heuristicCalculation(Complex[] data, int start,
					int end) {
				return data[(start + end) / 2].abs();
			}

			@Override
			public double heuristicCalculation(double[] data, int start,
					int end) {
				return data[(start + end) / 2];
			}

			@Override
			public int heuristicCalculationIndex(Complex[] data, int start,
					int end) {
				return (start + end) / 2;
			}

			@Override
			public int heuristicCalculationIndex(double[] data, int start,
					int end) {
				return (start + end) / 2;
			}
		});
	}
}

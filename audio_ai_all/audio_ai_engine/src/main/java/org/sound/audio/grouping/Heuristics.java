package org.sound.audio.grouping;

import java.util.HashMap;
import java.util.Map;

import org.sound.audio.fft.Complex;

public class Heuristics {

    public static interface HeuristicCalculation {
        /**
         * Use the double[] implementation with pre-calculated magnitudes
         */
        @Deprecated
        Frequency heuristicCalculation(Complex[] data, int start, int end);

        Frequency heuristicCalculation(double[] data, int start, int end);

        Frequency heuristicCalculation(Frequency[] data, int start, int end);
    }

    public static enum HEURISTIC {
        BIGGEST, MEDIAN, MEAN
    }

    public static final Map<HEURISTIC, HeuristicCalculation> HEURISTICS_IMPL = new HashMap<HEURISTIC, HeuristicCalculation>();

    private static double[] getMagnitudes(Complex[] data) {
        double[] magnitudes = new double[data.length];
        for (int frequency = 0; frequency < data.length; frequency++) {
            magnitudes[frequency] = data[frequency].abs();
        }
        return magnitudes;
    }

    private static double[] getMagnitudes(Frequency[] data) {
        double[] magnitudes = new double[data.length];
        for (int freqBin = 0; freqBin < data.length; freqBin++) {
            magnitudes[freqBin] = data[freqBin].magnitude;
        }
        return magnitudes;
    }

    static {
        HEURISTICS_IMPL.put(HEURISTIC.BIGGEST, new HeuristicCalculation() {

            /**
             * Use the double[] implementation with pre-calculated magnitudes
             */
            @Deprecated
            @Override
            public Frequency heuristicCalculation(Complex[] data, int start, int end) {
                return heuristicCalculation(getMagnitudes(data), start, end);
            }

            @Override
            public Frequency heuristicCalculation(Frequency[] data, int start, int end) {
                return heuristicCalculation(getMagnitudes(data), start, end);
            }

            @Override
            public Frequency heuristicCalculation(double[] data, int start, int end) {
                double biggestMagnitude = data[start];
                int biggestFrequency = start;
                for (int i = start + 1; i < end; i++) {
                    if (biggestMagnitude < data[i]) {
                        biggestMagnitude = data[i];
                        biggestFrequency = i;
                    }
                }
                return new Frequency(biggestFrequency, biggestMagnitude);
            }

        });

        HEURISTICS_IMPL.put(HEURISTIC.MEAN, new HeuristicCalculation() {

            /**
             * Use the double[] implementation with pre-calculated magnitudes
             */
            @Deprecated
            @Override
            public Frequency heuristicCalculation(Complex[] data, int start, int end) {
                return heuristicCalculation(getMagnitudes(data), start, end);
            }

            @Override
            public Frequency heuristicCalculation(Frequency[] data, int start, int end) {
                return heuristicCalculation(getMagnitudes(data), start, end);
            }

            @Override
            public Frequency heuristicCalculation(double[] data, int start, int end) {
                double sum = 0.0;
                for (int i = start; i < end; i++) {
                    sum += data[i];
                }
                return new Frequency((start + end) / 2, sum / (end - start));
            }

        });

        HEURISTICS_IMPL.put(HEURISTIC.MEDIAN, new HeuristicCalculation() {

            /**
             * Use the double[] implementation with pre-calculated magnitudes
             */
            @Deprecated
            @Override
            public Frequency heuristicCalculation(Frequency[] data, int start, int end) {
                return heuristicCalculation(getMagnitudes(data), start, end);
            }
            
            @Override
            public Frequency heuristicCalculation(Complex[] data, int start, int end) {
                return heuristicCalculation(getMagnitudes(data), start, end);
            }

            @Override
            public Frequency heuristicCalculation(double[] data, int start, int end) {
                return new Frequency((start + end) / 2, data[(start + end) / 2]);
            }
        });
    }
}

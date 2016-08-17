package org.sound.audio.hashextract;

import org.sound.audio.fftparser.FFTAudioInfo;
import org.sound.audio.grouping.FrequencyScale;
import org.sound.audio.grouping.LinearFrequencyScale;

import static org.sound.audio.grouping.Heuristics.*;

import java.util.Arrays;

public class KeyPointsExtractor {

    private FrequencyScale groupingFactor;

    private FFTAudioInfo fftAudioInfo;

    public KeyPointsExtractor(FFTAudioInfo fftAudioInfo, FrequencyScale groupingFactor) {
        this.groupingFactor = groupingFactor;
        this.fftAudioInfo = fftAudioInfo;
    }

    public int[][] getSignificantFrequenciesPerBucket(int limitToNthBucket) {
        double[][] magnitudeMap = fftAudioInfo.getMagnitudeMap();
        int result[][] = new int[magnitudeMap.length][];
        int bucketCount = limitToNthBucket <= 0 ? groupingFactor.getGroups() : limitToNthBucket;
        bucketCount = Math.min(bucketCount, groupingFactor.getGroups());
        System.out.printf("Executing for %d buckets\n", bucketCount);
        for (int resultLine = 0; resultLine < magnitudeMap.length; resultLine++) {
            result[resultLine] = new int[bucketCount];
        }
        int[] scales = groupingFactor.getGroupingLimits();
        System.out.println("Scaling is : " + Arrays.toString(scales));
        HeuristicCalculation calc = HEURISTICS_IMPL.get(HEURISTIC.BIGGEST);
        for (int magnitudeLine = 0; magnitudeLine < magnitudeMap.length; magnitudeLine++) {
            // Remember - scales are always group + 1;
            for (int bucket = 0; bucket < bucketCount; bucket++) {
                result[magnitudeLine][bucket] = calc.heuristicCalculationIndex(magnitudeMap[magnitudeLine],
                        scales[bucket], scales[bucket + 1]);
            }
        }
        return result;
    }

    // TODO
    public int[][] getSignificantFrequenciesPerBucket() {
        return getSignificantFrequenciesPerBucket(0);
    }

    public int[][] getSignificantNBiggest(int limitTo) {
        double[][] magnitudeMap = fftAudioInfo.getMagnitudeMap();
        int[][] biggestBuckets = new int[magnitudeMap.length][];
        for (int frame = 0; frame < biggestBuckets.length; frame++) {
            biggestBuckets[frame] = getSignificant(magnitudeMap[frame], limitTo);
        }
        return biggestBuckets;
    }

    public int[] getSignificant(double[] magnitudes, int limitTo) {
        double[] workArray = Arrays.copyOf(magnitudes, magnitudes.length);
        int resultSize = limitTo < magnitudes.length ? limitTo : workArray.length;
        int[] result = new int[resultSize];
        for (int itteration = 0; itteration < resultSize; itteration++) {
            // Find current MAX;
            int maxIndex = 0;
            double maxValue = workArray[maxIndex];
            for (int i = 1; i < workArray.length; i++) {
                if (maxValue < workArray[i]) {
                    maxValue = workArray[i];
                    maxIndex = i;
                }
            }
            result[itteration] = maxIndex;
            workArray[maxIndex] = Double.MIN_VALUE;
        }
        return result;
    }

    public static void main(String[] args) {
        int MAX = 32;
        double[][] magnitudes = new double[MAX / 4][MAX];
        for (int i = 0; i < MAX / 4; i++) {
            for (int j = 0; j < MAX; j++) {
                magnitudes[i][j] = Math.ceil(i * MAX * Math.random() + j);
            }
        }
        System.out.println("Initial");
        for (int i = 0; i < magnitudes.length; i++) {
            for (int j = 0; j < magnitudes[i].length; j++) {
                System.out.print(magnitudes[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Execute");
        FFTAudioInfo info = new FFTAudioInfo(magnitudes);
        KeyPointsExtractor k = new KeyPointsExtractor(info, new LinearFrequencyScale(4, 0, 32));
        int[][] result = k.getSignificantFrequenciesPerBucket(8);
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }

        int[][] biggest = k.getSignificantNBiggest(4);
        System.out.println("BIGGEST: ");
        for (int i = 0; i < biggest.length; i++) {
            for (int j = 0; j < biggest[i].length; j++) {
                System.out.print(biggest[i][j] + " ");
            }
            System.out.println();
        }
    }

}

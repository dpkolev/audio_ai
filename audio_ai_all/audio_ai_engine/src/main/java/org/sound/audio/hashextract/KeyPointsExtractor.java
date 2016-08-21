package org.sound.audio.hashextract;

import org.sound.audio.fftparser.FFTAudioInfo;
import org.sound.audio.grouping.BucketDiscretization;
import org.sound.audio.grouping.BucketScaling;
import org.sound.audio.grouping.Frequency;
import org.sound.audio.grouping.BaseScale;
import org.sound.audio.grouping.LinearFrequencyScale;

import static org.sound.audio.grouping.Heuristics.*;

import java.util.Arrays;

public class KeyPointsExtractor {

    private BaseScale groupingFactor;

    private FFTAudioInfo fftAudioInfo;

    public KeyPointsExtractor(FFTAudioInfo fftAudioInfo, BaseScale groupingFactor) {
        this.groupingFactor = groupingFactor;
        this.fftAudioInfo = fftAudioInfo;
    }

    // TODO
    public Frequency[][] getSignificantFrequenciesPerBucket() {
        return getSignificantFrequenciesPerBucket(0);
    }

    public Frequency[][] getSignificantFrequenciesPerBucket(int limitToNthBucket) {
        double[][] magnitudeMap = fftAudioInfo.getMagnitudeMap();
        Frequency[][] result = new Frequency[magnitudeMap.length][];
        int bucketCount = limitToNthBucket <= 0 ? groupingFactor.getGroups() : limitToNthBucket;
        bucketCount = Math.min(bucketCount, groupingFactor.getGroups());
        System.out.printf("Executing for %d buckets\n", bucketCount);
        for (int resultLine = 0; resultLine < magnitudeMap.length; resultLine++) {
            result[resultLine] = new Frequency[bucketCount];
        }
        int[] scales = groupingFactor.getGroupingLimits();
        // TODO Debug
        System.out.println("Scaling is : " + Arrays.toString(scales));
        System.out.println("Magnitude line length is: " + magnitudeMap[0].length);
        HeuristicCalculation calc = HEURISTICS_IMPL.get(HEURISTIC.BIGGEST);
        for (int magnitudeLine = 0; magnitudeLine < magnitudeMap.length; magnitudeLine++) {
            // Remember - scales are always group + 1;
            for (int bucket = 0; bucket < bucketCount; bucket++) {
                result[magnitudeLine][bucket] = calc.heuristicCalculation(magnitudeMap[magnitudeLine], scales[bucket],
                        scales[bucket + 1]);
            }
        }
        return result;
    }

    public Frequency[][] getBiggestPerBandBuckets() {
        Frequency[][] significantPerBucket = getSignificantFrequenciesPerBucket();
        BucketDiscretization bDiscret = new BucketDiscretization();
        BucketScaling bucketScaler = new BucketScaling();
        Frequency[][] biggestBuckets = new Frequency[significantPerBucket.length][];
        for (int frame = 0; frame < biggestBuckets.length; frame++) {
            // TODO Debug
            System.out.println(Arrays.toString(significantPerBucket[frame]));
            biggestBuckets[frame] = getSignificant(significantPerBucket[frame], bDiscret, bucketScaler);
            // TODO Debug
            System.out.println();
        }
        return biggestBuckets;
    }

    private Frequency[] getSignificant(Frequency[] binFrame, BucketDiscretization bDiscret, BucketScaling scaler) {
        Frequency[] result = new Frequency[scaler.getGroups()];
        int[] groupingLimits = scaler.getGroupingLimits(); // {0,10,20,40,80,160,512};
        HeuristicCalculation calc = HEURISTICS_IMPL.get(HEURISTIC.BIGGEST);
        // Remember - scales are always group + 1;
        for (int bucket = 0; bucket < scaler.getGroups(); bucket++) {
            result[bucket] = calc.heuristicCalculation(binFrame, groupingLimits[bucket], groupingLimits[bucket + 1]);
            // TODO Debug
            System.out.print(binFrame[result[bucket].frequency] + " ");
        }
        // TODO Debug
        System.out.println();
        // TODO Debug
        System.out.println(Arrays.toString(result));
        double sum = 0;
        int usedBins = 0;
        double mid = 0;
        for (int bin = 0; bin < result.length; bin++) {
            if (binFrame[bin].magnitude != 0) {
                sum += result[bin].magnitude;
                usedBins++;
            }
        }
        if (usedBins > 0) {
            mid = sum / usedBins;
        }
        // TODO Debug
        System.out.println("Mid is " + mid);
        for (int bin = 0; bin < result.length; bin++) {
            if (result[bin].magnitude < mid) {
                result[bin].frequency = 0;
                result[bin].magnitude = 0.0;
            }
        }
        // TODO Debug
        System.out.println(Arrays.toString(result));
        return result;
    }

    public static void main(String[] args) {
        int windowSize = 4096;
        int frames = windowSize / 512;
        int MAX_SEED_VAL = 1000;
        double[][] magnitudes = new double[frames][windowSize];
        for (int i = 0; i < frames; i++) {
            for (int j = 0; j < windowSize; j++) {
                magnitudes[i][j] = Math.ceil(MAX_SEED_VAL * Math.random());
            }
        }
        magnitudes[0][12] = 99999;
        System.out.println("Initial");
        for (int i = 0; i < magnitudes.length; i++) {
            for (int j = 0; j < magnitudes[i].length; j++) {
                System.out.print(magnitudes[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Execute");
        FFTAudioInfo info = new FFTAudioInfo(magnitudes);
        KeyPointsExtractor k = new KeyPointsExtractor(info,
                new LinearFrequencyScale(windowSize / frames, 0, windowSize));
        Frequency[][] result = k.getBiggestPerBandBuckets();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }

}

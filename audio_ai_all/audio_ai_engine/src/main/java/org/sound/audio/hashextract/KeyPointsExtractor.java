package org.sound.audio.hashextract;

import org.sound.audio.fftparser.FFTAudioInfo;
import org.sound.audio.grouping.FrequencyScale;
import org.sound.audio.grouping.LinearFrequencyScale;

import static org.sound.audio.grouping.Heuristics.*;

import java.util.Arrays;

public class KeyPointsExtractor {

	private FrequencyScale groupingFactor;

	private FFTAudioInfo fftAudioInfo;

	public KeyPointsExtractor(FFTAudioInfo fftAudioInfo,
			FrequencyScale groupingFactor) {
		this.groupingFactor = groupingFactor;
		this.fftAudioInfo = fftAudioInfo;
	}

	public int[][] getSignificantFrequenciesPerBucket(int limitToNthBucket) {
		double[][] magnitudeMap = fftAudioInfo.getMagnitudeMap();
		int result[][] = new int[magnitudeMap.length][];
		int bucketCount = limitToNthBucket == 0
				? groupingFactor.getGroups()
				: limitToNthBucket;
		for (int resultLine = 0; resultLine < magnitudeMap.length; resultLine++) {
			result[resultLine] = new int[bucketCount];
		}
		int[] scales = groupingFactor.getGroupingLimits();
		HeuristicCalculation calc = HEURISTICS_IMPL.get(HEURISTIC.BIGGEST);
		for (int magnitudeLine = 0; magnitudeLine < magnitudeMap.length; magnitudeLine++) {
			// Remember - scales  are always group + 1;
			for (int bucket = 0; bucket < bucketCount - 1; bucket++) {
				result[magnitudeLine][bucket] = calc.heuristicCalculationIndex(
						magnitudeMap[magnitudeLine], 
						scales[bucket],
						scales[bucket + 1]);
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		int MAX = 256;
		double[][] magnitudes = new double[MAX][MAX];
		for (int i = 0; i < MAX; i++) {
			for (int j = 0; j < MAX; j++) {
				magnitudes[i][j] = i*MAX + j;
			}
		}
		FFTAudioInfo info = new FFTAudioInfo(magnitudes);
		KeyPointsExtractor k = new KeyPointsExtractor(info, new LinearFrequencyScale(8, 0, MAX));
		int[][] result = k.getSignificantFrequenciesPerBucket(8);
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				System.out.print(result[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	
}

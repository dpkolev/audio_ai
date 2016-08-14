package org.sound.audio.hashextract;

import org.sound.audio.fftparser.FFTAudioInfo;
import org.sound.audio.grouping.FrequencyScale;
import static org.sound.audio.grouping.Heuristics.*;

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
			for (int bucket = 0; bucket < scales.length; bucket++) {
				result[magnitudeLine][bucket] = calc.heuristicCalculationIndex(
						magnitudeMap[magnitudeLine], 
						scales[bucket],
						scales[bucket + 1]);
			}
		}
		return result;
	}
}

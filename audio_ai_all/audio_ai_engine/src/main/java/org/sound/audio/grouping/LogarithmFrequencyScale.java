package org.sound.audio.grouping;

import java.util.Arrays;

import javax.management.RuntimeErrorException;

public class LogarithmFrequencyScale extends FrequencyScale {

	public LogarithmFrequencyScale(int groups, int lowerLimit, int upperLimit) {
		super(groups, lowerLimit, upperLimit);
	}

	private static final int[] DEFAULT = new int[]{0};

	@Override
	public int[] getGroupingLimits() {
		int lowerLimit = getLowerLimit();
		int upperLimit = getUpperLimit();
		int groups = getGroups();
		if (lowerLimit < 0 || lowerLimit >= upperLimit || groups < 1) {
			return DEFAULT;
		}
		int range = upperLimit - lowerLimit;
		if (groups > Math.sqrt(2*range)) {
			throw new RuntimeException("Cannot create log grouping limits with delta increment < 1 !!!");
		}
		double temp = (2*range)/(groups * (groups+1));
		int deltaIncrementStep = (int)temp;
		System.out.println("DeltaIncrementStep is " + deltaIncrementStep);
		
		int[] groupLimits = new int[groups + 1];
		groupLimits[0] = lowerLimit;
		for (int currGroup = 1; currGroup < groups; currGroup++) {
			groupLimits[currGroup] = lowerLimit + (currGroup*(currGroup+1)/2)*deltaIncrementStep;
		}
		groupLimits[groups] = upperLimit;
		return groupLimits;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(
				new LogarithmFrequencyScale(48, 16, 4096).getGroupingLimits()));
	}

}

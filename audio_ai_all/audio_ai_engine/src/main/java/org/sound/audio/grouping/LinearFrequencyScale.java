package org.sound.audio.grouping;

import java.util.Arrays;

public class LinearFrequencyScale extends FrequencyScale {

	private static final int[] DEFAULT = new int[]{0};

	public LinearFrequencyScale(int groups, int lowerLimit,
			int upperLimit) {
		super(groups, lowerLimit, upperLimit);
	}

	@Override
	public int[] getGroupingLimits() {
		int lowerLimit = getLowerLimit();
		int upperLimit = getUpperLimit();
		int groups = getGroups();
		if (lowerLimit < 0 || lowerLimit >= upperLimit || groups < 1) {
			return DEFAULT;
		}
		int range = upperLimit - lowerLimit;
		int groupSize = range / groups;
		if (groupSize == 0) {
			return DEFAULT;
		}
		int[] groupLimits = new int[groups + 1];
		groupLimits[0] = lowerLimit;
		double groupDivisionRemainer = (range * 1.0 / groups) - groupSize;
		System.out.println(groupDivisionRemainer);
		double moduleHolder = 0;
		for (int currGroup = 1; currGroup < groups; currGroup++) {
			groupLimits[currGroup] = groupLimits[currGroup - 1] + groupSize;
			moduleHolder += groupDivisionRemainer;
			if (moduleHolder > 1) {
				groupLimits[currGroup] = groupLimits[currGroup] + 1;
				moduleHolder -= 1;
			}
		}
		groupLimits[groups] = upperLimit;
		return groupLimits;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(
				new LinearFrequencyScale(10, 5, 413).getGroupingLimits()));
	}
}

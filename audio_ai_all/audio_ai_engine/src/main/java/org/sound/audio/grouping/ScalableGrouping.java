package org.sound.audio.grouping;

public interface ScalableGrouping {
	
	public int[] getGroupingLimits(int groups, int lowerLimit, int upperLimit);
}

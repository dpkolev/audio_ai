package org.sound.audio.grouping;

public interface ScalableGrouping {
	
	public int[] getGroupingLimits();
	
	public int getGroups();
	
	public int getLowerLimit();
	
	public int getUpperLimit();
}

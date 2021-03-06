package org.sound.audio.grouping;

public abstract class BaseScale implements ScalableGrouping {

	private int groups, lowerLimit, upperLimit;

	public BaseScale(int groups, int lowerLimit, int upperLimit) {
		this.groups = groups;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}

	@Override
	public int getGroups() {
		return this.groups;
	}

	@Override
	public int getLowerLimit() {
		return this.lowerLimit;
	}

	@Override
	public int getUpperLimit() {
		return this.upperLimit;
	}
}

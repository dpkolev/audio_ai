package org.sound.audio.grouping;

public class BucketScaling extends BaseScale {
    
    private static final int[] SCALES = {0,10,20,40,80,160,512};
    
    public BucketScaling() {
        
        super(SCALES.length-1, SCALES[0], SCALES[SCALES.length-1]);
    }

    @Override
    public int[] getGroupingLimits() {
        return SCALES;
    }
}

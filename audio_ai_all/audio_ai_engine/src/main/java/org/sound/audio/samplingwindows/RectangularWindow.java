package org.sound.audio.samplingwindows;

import java.util.Arrays;

public class RectangularWindow extends BaseWindow {

    public RectangularWindow(int size) {
        super(size);
    }

    @Override
    protected double[] calculateWindowImpl() {
        double[] window = new double[getSize()];
        Arrays.fill(window, 1.0);
        return window;
    }

    
}

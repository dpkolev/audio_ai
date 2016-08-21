package org.sound.audio.samplingwindows;

public class HammingWindow extends BaseWindow {

    public HammingWindow(int size) {
        super(size);
    }

    @Override
    protected double[] calculateWindowImpl() {
        int size = getSize();
        double[] window = new double[size];
        for (int i = 0; i < window.length; i++) {
            window[i] = 0.54 - 0.46 * (Math.cos(2 * Math.PI * i / size));
        }
        return window;
    }

}

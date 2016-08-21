package org.sound.audio.samplingwindows;

public class BlackmanWindow extends BaseWindow {

    public BlackmanWindow(int size) {
        super(size);
    }

    @Override
    protected double[] calculateWindowImpl() {
        int size = getSize();
        double[] window = new double[size];
        for (int i = 0; i < window.length; i++) {
            window[i] = 0.42 - 0.5 * Math.cos(2 * Math.PI * i / (size - 1))
                    + 0.08 * Math.cos(4 * Math.PI * i / (size - 1));
        }
        return window;
    }

}

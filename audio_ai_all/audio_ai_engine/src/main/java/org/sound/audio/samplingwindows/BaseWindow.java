package org.sound.audio.samplingwindows;

public abstract class BaseWindow implements WindowFunction {

    private double[] _windowCoef;
    private int size;

    public BaseWindow(int size) {
        super();
        this.size = size;
        this._windowCoef = calculateWindowImpl();
    }

    abstract protected double[] calculateWindowImpl();

    @Override
    public double[] getWindow() {
        return this._windowCoef;
    }

    public int getSize() {
        return this.size;
    }
}

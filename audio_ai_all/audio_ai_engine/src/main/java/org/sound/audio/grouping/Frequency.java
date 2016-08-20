package org.sound.audio.grouping;

public class Frequency {
    public int frequency;
    public double magnitude;
    
    public Frequency(int frequency, double magnitude) {
        super();
        this.frequency = frequency;
        this.magnitude = magnitude;
    }
    
    public Frequency() {
        this(0,0.0);
    }
    
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[" + this.frequency + " " + this.magnitude + "]";
    }
}

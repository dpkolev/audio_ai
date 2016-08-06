package org.sound.audio.fftparser;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.sound.audio.fft.Complex;

public class FFTVisualizerFrame extends JFrame implements Runnable {

	private FFTVisualizerPanel spectroPanel;

	
	public FFTVisualizerFrame(Complex[][] fft, boolean logModeEnabled) {
		this(new FFTVisualizerPanel(fft, logModeEnabled));
	}
	
	FFTVisualizerFrame(FFTVisualizerPanel spectrogramPanel) {
		this.spectroPanel = spectrogramPanel;
		init();
	}

	private void init() {
		GridLayout layout = new GridLayout(1, 1);
		setLayout(layout);
		JScrollPane srcPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		srcPane.add(spectroPanel);
		add(srcPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		this.spectroPanel.paint(g);
	}

	@Override
	public void run() {
	}
}

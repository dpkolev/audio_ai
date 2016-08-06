package org.sound.audio.fftparser;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PopUpJFrame extends JFrame {

	private static final long serialVersionUID = 2666267046217107532L;

	private Image img;

	private int size_x, size_y;

	public PopUpJFrame(Image img, String windowLabel) {
		this((BufferedImage) img, windowLabel);
	}

	public PopUpJFrame(BufferedImage img, String windowLabel) {
		if (img != null) {
			this.img = img;
			this.size_x = img.getWidth();
			this.size_y = img.getHeight();
			if (this.size_x < 0) {
				this.size_x = 800;
			}
			if (this.size_y < 0) {
				this.size_y = 600;
			}
			setSize(this.size_x, this.size_y);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			if (windowLabel != null && windowLabel.trim() != ""){
				this.setTitle(windowLabel);
			} else {
				this.setTitle("PopUp");
			}
		}
		setContentPane(new JLabel(new ImageIcon(this.img)));
		this.setVisible(true);
	}
}

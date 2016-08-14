package org.sound.audio;

/*
 * 11/19/04		1.0 moved to LGPL.
 * 29/01/00		Initial version. mdm@techie.com
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;

/**
 * The <code>Player</code> class implements a simple player for playback of an
 * MPEG audio stream.
 * 
 * @author Mat McGowan
 * @since 0.0.8
 */

// REVIEW: the audio device should not be opened until the
// first MPEG audio frame has been decoded.
public class AudioSample extends Player {
	public AudioSample(InputStream stream) throws JavaLayerException {
		this(stream, null);
	}

	/**
	 * The current frame number.
	 */
	private int frame = 0;

	/**
	 * The MPEG audio bitstream.
	 */
	// javac blank final bug.
	/* final */private Bitstream bitstream;

	/**
	 * The MPEG audio decoder.
	 */
	/* final */private Decoder decoder;

	/**
	 * The AudioDevice the audio samples are written to.
	 */
	private AudioDevice audio;

	/**
	 * Has the player been closed?
	 */
	private boolean closed = false;

	/**
	 * Has the player played back all frames from the stream?
	 */
	private boolean complete = false;

	private int lastPosition = 0;

	public AudioSample(InputStream stream, AudioDevice device)
			throws JavaLayerException {
		super(stream, device);
		bitstream = new Bitstream(stream);
		decoder = new Decoder();

		if (device != null) {
			audio = device;
		} else {
			FactoryRegistry r = FactoryRegistry.systemRegistry();
			audio = r.createAudioDevice();
		}
		audio.open(decoder);
	}

	public void play() throws JavaLayerException {
		play(Integer.MAX_VALUE);
	}

	/**
	 * Plays a number of MPEG audio frames.
	 * 
	 * @param frames
	 *            The number of frames to play.
	 * @return true if the last frame was played, or false if there are more
	 *         frames.
	 */
	public boolean play(int frames) throws JavaLayerException {
		boolean ret = true;

		while (frames-- > 0 && ret) {
			ret = decodeFrame();
		}

		if (!ret) {
			// last frame, ensure all data flushed to the audio device.
			AudioDevice out = audio;
			if (out != null) {
				out.flush();
				synchronized (this) {
					complete = (!closed);
					close();
				}
			}
		}
		return ret;
	}

	/**
	 * Cloases this player. Any audio currently playing is stopped immediately.
	 */
	public synchronized void close() {
		AudioDevice out = audio;
		if (out != null) {
			closed = true;
			audio = null;
			// this may fail, so ensure object state is set up before
			// calling this method.
			out.close();
			lastPosition = out.getPosition();
			try {
				bitstream.close();
			} catch (BitstreamException ex) {
			}
		}
	}

	/**
	 * Returns the completed status of this player.
	 * 
	 * @return true if all available MPEG audio frames have been decoded, or
	 *         false otherwise.
	 */
	public synchronized boolean isComplete() {
		return complete;
	}

	/**
	 * Retrieves the position in milliseconds of the current audio sample being
	 * played. This method delegates to the <code>
	 * AudioDevice</code> that is used by this player to sound the decoded audio
	 * samples.
	 */
	public int getPosition() {
		int position = lastPosition;

		AudioDevice out = audio;
		if (out != null) {
			position = out.getPosition();
		}
		return position;
	}

	/**
	 * Decodes a single frame.
	 * 
	 * @return true if there are no more frames to decode, false otherwise.
	 */
	protected boolean decodeFrame() throws JavaLayerException {
		try {
			AudioDevice out = audio;
			if (out == null)
				return false;

			Header h = bitstream.readFrame();

			if (h == null)
				return false;

			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h,
					bitstream);
			try {
				byte[] left = new byte[output.getBuffer().length];
				byte[] right = new byte[output.getBuffer().length];
				int lIndex =0 , rIndex = 0;
				for (int itt = 0; itt < output.getBuffer().length; itt+=2) {
					//int slot = 1044;
					short ls = output.getBuffer()[itt];
					short rs = output.getBuffer()[itt+1];
					left[lIndex++] = (byte)ls;
					left[lIndex++] = (byte)(ls>>>8);
					right[rIndex++] = (byte)rs;
					right[rIndex++] = (byte)(rs>>>8);
					//output.getBuffer()[itt+1] = output.getBuffer()[itt]; // to MONO
					//output.getBuffer()[itt+1] = 0; // left channel
					//output.getBuffer()[itt] = 0; // right channel
//					if (itt == slot) {
//						output.getBuffer()[itt] = Short.MAX_VALUE;
//						output.getBuffer()[itt+1] = Short.MAX_VALUE;
//					} else {
//						output.getBuffer()[itt] = 0;
//						output.getBuffer()[itt+1] = 0;
//					}
				}
				
				fr.write(Arrays.toString(left));
				fr.write("\n");
				fr.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			synchronized (this) {
//				out = audio;
//				if (out != null) {
//					out.write(output.getBuffer(), 0, output.getBufferLength());
//				}
//			}

			bitstream.closeFrame();
		} catch (RuntimeException ex) {
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
		/*
		 * catch (IOException ex) { System.out.println(
		 * "exception decoding audio frame: "+ex); return false; } catch
		 * (BitstreamException bitex) { System.out.println(
		 * "exception decoding audio frame: "+bitex); return false; } catch
		 * (DecoderException decex) { System.out.println(
		 * "exception decoding audio frame: "+decex); return false; }
		 */
		return true;
	}

	public static FileWriter fr;

	//public static File f = new File(AudioSample.class.getClassLoader().getResource("test1_celldweller_through_the_gates.mp3").getFile());
	public static File f = new File(AudioSample.class.getClassLoader().getResource("test_300_Hz_Sine_Wave_Sound_Frequency_Tone.mp3").getFile());
	
	public static void main(String[] args) throws Exception {

		System.out.println(AudioSystem.isLineSupported(Port.Info.SPEAKER));
		fr = new FileWriter(new File("out.txt"));
		// int sampleRate = 44100;
		// int sampleSizeInBits = 16;
		// int channels = 2;
		// boolean isSigned = true;
		// boolean isBigEndian = true;
		// AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
		// channels, isSigned, isBigEndian);
		// TargetDataLine line = AudioSystem.getTargetDataLine(format);
		// AudioInputStream ais = new AudioInputStream(line);

		//
		// AudioInputStream ais2 = AudioSystem.getAudioInputStream(new File(
		// "res/celldweller-through_the_gates.mp3"));
		// Clip clip = AudioSystem.getClip();
		// clip.open(ais2);
		// clip.start();
		AudioInputStream aisPlayer = AudioSystem.getAudioInputStream(f);
		AudioSample player = new AudioSample(aisPlayer);
		player.play();
	}
}

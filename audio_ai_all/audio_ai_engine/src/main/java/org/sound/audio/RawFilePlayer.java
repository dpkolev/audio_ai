package org.sound.audio;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.test.TestPlayRaw;

public class RawFilePlayer {
	public static final String DEFAULT_FILE_TO_PLAY = "test3_orjan_nilsen_so_long_radio.mp3"; // "test4_blue_stahli_anti_you.wav";

	private static final String OUT_RAW = "out_raw.txt";

	public static final int OUT_BUFFER_SIZE = 2 * 1024; // 2 * 1152;

	private String filename;

	private Channel leftChannel;

	private Channel rightChannel;

	private boolean isPlayed;

	public RawFilePlayer(String filename) {
		this.filename = filename;
	}

	public RawFilePlayer() {
		this(RawFilePlayer.class.getClassLoader()
				.getResource(DEFAULT_FILE_TO_PLAY).getFile());
	}

	public String getFilename() {
		return filename;
	}

	public Channel getLeftChannel() {
		return leftChannel;
	}

	public Channel getRightChannel() {
		return rightChannel;
	}

	public boolean isPlayed() {
		return isPlayed;
	}

	public void playback() {
		try {
			File file = new File(this.filename);
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
					16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			// Play now.
			prepareChannels(baseFormat.getChannels());
			rawexctract(decodedFormat, din);
			in.close();
			isPlayed = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareChannels(int channels) {
		if (channels == 1) {
			this.leftChannel = new Channel(ChannelType.MONO);
			this.rightChannel = new Channel(ChannelType.MONO);
		} else if (channels == 2) {
			this.leftChannel = new Channel(ChannelType.LEFT);
			this.rightChannel = new Channel(ChannelType.RIGHT);
		} else {
			throw new IllegalArgumentException(
					"Can support only mono or 2 channels");
		}
	}

	private void rawexctract(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException {
		int frameSize = targetFormat.getFrameSize();
		int audioChannels = targetFormat.getChannels();
		PrintWriter pw = new PrintWriter(new File(OUT_RAW));
		byte[] data = new byte[audioChannels * OUT_BUFFER_SIZE];// new
																// byte[4096];
		System.out.println("FRAMESIZE IS: " + frameSize);
		System.out.println("AUDIO CHANNELS ARE: " + audioChannels);
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			int count = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				pw.println(Arrays.toString(data));
				if (audioChannels == 1) {
					this.leftChannel.appendByteData(data);
					this.rightChannel.appendByteData(data);
				} else {
					Byte[] lChannel = new Byte[data.length / 2];
					Byte[] rChannel = new Byte[data.length / 2];
					int lIndex = 0;
					int rIndex = 0;
					if (data.length % frameSize != 0) {
						pw.close();
						throw new RuntimeException(
								"Bytestream size MUST be divider of current frameSize: "
										+ frameSize);
					}
					for (int i = 0; i < data.length / frameSize; i++) {
						lChannel[lIndex++] = data[frameSize * i];
						lChannel[lIndex++] = data[frameSize * i + 1];
						rChannel[rIndex++] = data[frameSize * i + 2];
						rChannel[rIndex++] = data[frameSize * i + 3];
					}
					this.leftChannel.getChannelData().add(lChannel);
					this.rightChannel.getChannelData().add(rChannel);
				}
				for (int i = 0; i < data.length; i++) {
					// if (i%4 == 2 || i%4 == 3) {data[i] = 0;} // Left Channel
					// if (i%4 == 0 || i%4 == 1) {data[i] = 0;} // Right Channel
				}
				for (int i = 0; i < data.length; i += frameSize) {
					// data[i] = data[i+2];data[i+1]=data[i+3]; //Right side
					// MONO
					// data[i] = data[i+2];data[i+1]=data[i+3]; //Right side
					// MONO
				}
				System.out.println(count++ + " -> " + nBytesRead);
				// if (nBytesRead != -1) {
				// nBytesWritten = line.write(data, 0, nBytesRead);
				// }
			}
			this.leftChannel.serializeToFile("LEFT_sample.txt");
			this.rightChannel.serializeToFile("RIGHT_sample.txt");
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	private SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

}

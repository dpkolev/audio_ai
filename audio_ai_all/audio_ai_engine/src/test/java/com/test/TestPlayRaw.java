package com.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class TestPlayRaw {
	
	//TODO Change me, before testing
	private static final String FILE_TO_PLAY = "test3_orjan_nilsen_so_long_radio.mp3"; //"test4_blue_stahli_anti_you.wav";
	
	private static final String OUT_RAW = "out_raw.txt";
	
	public static final int	OUT_BUFFER_SIZE = 2 * 1152;

	public void testPlay(String filename) {
		try {
			File file = new File(filename);
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
					16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			// Play now.
			rawplay(decodedFormat, din);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException {
		PrintWriter pw = new PrintWriter(new File(OUT_RAW));
		byte[] data = new byte[OUT_BUFFER_SIZE];//new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			int count = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				pw.write(Arrays.toString(data) + "\n");
				for (int i = 0; i < data.length; i++) {
					//if (i%4 == 2 || i%4 == 3) {data[i] = 0;} // Left Channel
					//if (i%4 == 0 || i%4 == 1) {data[i] = 0;} // Right Channel
				}
				for (int i = 0; i < data.length; i+=4) {
					//data[i] = data[i+2];data[i+1]=data[i+3]; //Right side MONO
					//data[i] = data[i+2];data[i+1]=data[i+3]; //Right side MONO
				}
				System.out.println(count++ + " -> " + nBytesRead);
				if (nBytesRead != -1)
					nBytesWritten = line.write(data, 0, nBytesRead);
			}
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
	
	public static void main(String[] args) {
		URL url = TestPlayRaw.class.getClassLoader().getResource(FILE_TO_PLAY);
		if (url == null) {
			System.err.println("No stream found !!! Add resource from src/test/resources");
		} else {
			new TestPlayRaw().testPlay(url.getFile());
		}
	}
}

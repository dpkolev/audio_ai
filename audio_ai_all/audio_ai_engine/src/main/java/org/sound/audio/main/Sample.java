package org.sound.audio.main;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;

import org.tritonus.share.sampled.file.TAudioFileFormat;
public class Sample {

	public Sample() {
	}
	
	public static void main(String[] args) throws Exception {
		showMixers();
	}
	public static void showMixers() {
		ArrayList<Mixer.Info> mixInfos = new ArrayList<Mixer.Info>(
				Arrays.asList(AudioSystem.getMixerInfo()));
		Line.Info sourceDLInfo = new Line.Info(SourceDataLine.class);
		Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);
		Line.Info clipInfo = new Line.Info(Clip.class);
		Line.Info portInfo = new Line.Info(Port.class);
		String support;
		for (Mixer.Info mixInfo : mixInfos) {
			Mixer mixer = AudioSystem.getMixer(mixInfo);
			support = ", supports ";
			if (mixer.isLineSupported(sourceDLInfo))
				support += "SourceDataLine ";
			if (mixer.isLineSupported(clipInfo))
				support += "Clip ";
			if (mixer.isLineSupported(targetDLInfo))
				support += "TargetDataLine ";
			if (mixer.isLineSupported(portInfo))
				support += "Port ";
			System.out.println("Mixer: " + mixInfo.getName() + support + ", "
					+ mixInfo.getDescription());
		}
	}
	
	public static void getDurationWithMp3Spi(File file) throws UnsupportedAudioFileException, IOException {

	    AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
	    if (fileFormat instanceof TAudioFileFormat) {
	        Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
	        String key = "duration";
	        Long microseconds = (Long) properties.get(key);
	        int mili = (int) (microseconds / 1000);
	        //int sec = (mili / 1000) % 60;
	        //int min = (mili / 1000) / 60;
	        System.out.println(String.format("Time : %d ms",mili));
	    } else {
	        throw new UnsupportedAudioFileException();
	    }

	}
}

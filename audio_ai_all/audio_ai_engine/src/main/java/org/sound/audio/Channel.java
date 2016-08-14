package org.sound.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Channel {

	private static final int WINDOW_SIZE = 4096;

	private ChannelType channel;

	private List<Byte[]> channelData;

	public Channel(ChannelType type) {
		this(type, null);
	}

	public Channel(ChannelType type, List<Byte[]> rawData) {
		this.channel = type;
		this.channelData = rawData != null ? rawData : new ArrayList<Byte[]>();
	}

	public List<Byte[]> getChannelData() {
		return channelData;
	}

	public void setChannelData(List<Byte[]> channelData) {
		this.channelData = channelData;
	}

	public ChannelType getChannel() {
		return channel;
	}

	public void appendByteData(byte[] data) {
		Byte[] objData = new Byte[data.length];
		for (int i = 0; i < data.length; i++) {
			objData[i] = data[i];
		}
		getChannelData().add(objData);
	}

	public int getFrameSize() {
		if (getChannelData().size() == 0) {
			return -1;
		} else {
			return getChannelData().get(0).length;
		}
	}
	public void serializeToFile(String fileName) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(getChannel() + "_" +fileName));
		for (Byte[] b : getChannelData()) {
			pw.println(Arrays.toString(b));
		}
		pw.flush();
		pw.close();
	}

	public void recompactForFourierDoubleFrame() {
		if (this.channelData.isEmpty()) {
			return;
		}
		int itterations = WINDOW_SIZE / channelData.get(0).length;
		System.out.println("recompactForFourierDoubleFrame - " + channel);
		for (int itteration = 1; itteration < itterations; itteration++) {
			int dataSize = channelData.size();
			System.out.println("Current channel size: " + dataSize);
			List<Byte[]> mergeData;
			if (dataSize % 2 != 0) {
				Byte[] filler = new Byte[channelData.get(0).length];
				Arrays.fill(filler, (byte) 0);
				channelData.add(filler);
				dataSize = channelData.size();
				System.out.println(
						"Adding new empty frame - size is now: " + dataSize);
			}
			mergeData = new ArrayList<Byte[]>(dataSize / 2);
			for (int i = 0; i < dataSize; i += 2) {
				Byte[] frameOne = channelData.get(i);
				Byte[] frameTwo = channelData.get(i + 1);
				int frameOneLength = frameOne.length;
				int frameTwoLength = frameTwo.length;
				Byte[] mergedFrames = new Byte[frameOneLength + frameTwoLength];
				System.arraycopy(frameOne, 0, mergedFrames, 0, frameOneLength);
				System.arraycopy(frameTwo, 0, mergedFrames, frameOneLength,
						frameTwoLength);
				mergeData.add(mergedFrames);
			}
			channelData.clear();
			channelData.addAll(mergeData);
			System.out.println("After merge - size is " + channelData.size());
		}
	}
}

package org.sound.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Channel {
	private ChannelType channel;
	
	private List<Byte[]> channelData;
	
	public Channel(ChannelType type) {
		this(type, new ArrayList<Byte[]>());
	}
	
	public Channel(ChannelType type, List<Byte[]> rawData) {
		this.channel = type;
		this.channelData = rawData;
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
		for (int i = 0; i<data.length;i++) {
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
		PrintWriter pw = new PrintWriter(new File(fileName));
		for (Byte[] b : getChannelData()) {
			pw.println(Arrays.toString(b));
		}
		pw.flush();
		pw.close();
	}
}

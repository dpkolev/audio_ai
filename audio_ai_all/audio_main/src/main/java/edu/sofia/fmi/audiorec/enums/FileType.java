package edu.sofia.fmi.audiorec.enums;

public enum FileType {
	MP3(".mp3");
	String fileExt;
	
	private FileType(String fileExt) {
		this.fileExt = fileExt;
	}
	
	public String value() {
		return this.fileExt;
	}
}

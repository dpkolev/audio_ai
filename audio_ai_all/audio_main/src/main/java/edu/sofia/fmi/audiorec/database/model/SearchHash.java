package edu.sofia.fmi.audiorec.database.model;

public class SearchHash {
	
	private long hash;
	private long songId;
	public SearchHash(long hash, long songId) {
		super();
		this.hash = hash;
		this.songId = songId;
	}
	
	public SearchHash() {
		this(0L,0L);
	}

	public long getHash() {
		return hash;
	}

	public void setHash(long hash) {
		this.hash = hash;
	}

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("Hash: ").append(this.hash)
				.append(" |Song Id: ").append(this.songId)
				.toString();
	}
}

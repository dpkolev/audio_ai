package edu.sofia.fmi.audiorec.database.model;

import java.io.Serializable;

@Deprecated
public class SearchHashPK implements Serializable{

	private static final long serialVersionUID = -2538475188588023829L;
	
	private long searchHashId;
	private long songId;
	
	public SearchHashPK(long searchHashId, long songId) {
		super();
		this.searchHashId = searchHashId;
		this.songId = songId;
	}
	
	public long getSearchHashId() {
		return searchHashId;
	}

	public void setSearchHashId(long searchHashId) {
		this.searchHashId = searchHashId;
	}

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SearchHashPK) {
			return equals((SearchHashPK)obj);
		}
		return false;
	}

	private boolean equals(SearchHashPK obj) {
		return searchHashId == obj.searchHashId && songId == obj.songId;
	}
	
	@Override
	public int hashCode() {
		return (int)(searchHashId * songId);
	}
}

package edu.sofia.fmi.audiorec.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Song {

	@Id
	private long id;
	private String name;
	private String artist;
	private String location;

	public Song(long id, String name, String artist, String location) {
		super();
		this.id = id;
		this.name = name;
		this.artist = artist;
		this.location = location;
	}

	public Song() {
		this(0, null, null, null);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("ID: ").append(this.id)
				.append(" |name: ").append(this.name)
				.append(" |artist: ").append(this.artist)
				.append(" |location: ").append(this.location)
				.toString();
	}
}

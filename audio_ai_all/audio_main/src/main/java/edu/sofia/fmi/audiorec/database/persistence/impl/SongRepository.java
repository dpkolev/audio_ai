package edu.sofia.fmi.audiorec.database.persistence.impl;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.data.jpa.repository.Query;

import edu.sofia.fmi.audiorec.database.model.Song;
import edu.sofia.fmi.audiorec.database.persistence.base.BaseRepository;

public interface SongRepository extends BaseRepository<Song, Long> {

	List<Song> findByName(String name);

	List<Song> findByArtist(String artist);

	@Query("UPDATE Song s SET s.artist = :artist, s.name = :name, s.location = :location WHERE s.id = :id")
	Song update(@QueryParam("id") long songId,
			@QueryParam("artist") String artist,
			@QueryParam("name") String name,
			@QueryParam("location") String location);

}

package edu.sofia.fmi.audiorec.database.persistence;

import org.springframework.data.repository.CrudRepository;

import edu.sofia.fmi.audiorec.database.model.Song;

public interface SongRepository extends CrudRepository<Song, Long> {

}

package edu.sofia.fmi.audiorec.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.sofia.fmi.audiorec.database.model.Song;
import edu.sofia.fmi.audiorec.database.persistence.impl.SongRepository;
import edu.sofia.fmi.audiorec.service.SongService;

public class DefaultSongServiceImpl extends BaseServiceImpl<Song>
		implements
			SongService {

	@Autowired
	private SongRepository songRepository;
	
	public List<Song> findById(long id) {
		return songRepository.findById(id);
	}

	public List<Song> findAll() {
		return songRepository.findAll();
	}

	public Song save(Song entity) {
		return songRepository.save(entity);
	}

	public Song update(Song s) throws Exception {
		return songRepository.update(s.getId(), s.getArtist(), s.getName(), s.getLocation());
	}

	public void delete(Song entity) throws Exception {
		songRepository.delete(entity);
	}

	public void delete(long id) throws Exception {
		songRepository.delete(id);
	}
	
}

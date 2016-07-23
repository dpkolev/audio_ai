package edu.sofia.fmi.audiorec.database.persistence.impl;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.data.jpa.repository.Query;

import edu.sofia.fmi.audiorec.database.model.SearchHash;
import edu.sofia.fmi.audiorec.database.persistence.base.BaseRepository;

public interface SearchHashRepository extends BaseRepository<SearchHash, Long> {
	
	List<SearchHash> findBySongId(long songId);
	
	@Query("DELETE SearchHash sh WHERE sh.songId = :id")
	void delete(@QueryParam("hash") Long id);

}

package edu.sofia.fmi.audiorec.database.persistence.impl;

import java.util.List;

import edu.sofia.fmi.audiorec.database.model.SearchHash;
import edu.sofia.fmi.audiorec.database.persistence.base.BaseRepository;

public interface SearchHashRepository extends BaseRepository<SearchHash, Long> {
	
	List<SearchHash> findBySongId(long songId);
	
}

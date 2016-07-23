package edu.sofia.fmi.audiorec.service;

import java.util.List;

import edu.sofia.fmi.audiorec.database.model.SearchHash;

public interface SearchHashService extends BaseService<SearchHash> {
	
	void delete (Iterable<SearchHash> toDelete);
	
	List<SearchHash> findBySongId(long songId);

}

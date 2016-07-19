package edu.sofia.fmi.audiorec.service;

import edu.sofia.fmi.audiorec.database.model.SearchHash;

public interface SearchHashService extends BaseService<SearchHash> {
	
	void delete (Iterable<SearchHash> toDelete);

}

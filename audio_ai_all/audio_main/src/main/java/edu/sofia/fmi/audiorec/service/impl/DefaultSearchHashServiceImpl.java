package edu.sofia.fmi.audiorec.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.sofia.fmi.audiorec.database.model.SearchHash;
import edu.sofia.fmi.audiorec.database.persistence.impl.SearchHashRepository;
import edu.sofia.fmi.audiorec.service.SearchHashService;

public class DefaultSearchHashServiceImpl extends BaseServiceImpl<SearchHash>
		implements
			SearchHashService {

	@Autowired
	private SearchHashRepository searchHashRepository;

	public List<SearchHash> findById(long id) {
		return searchHashRepository.findById(id);
	}

	public List<SearchHash> findAll() {
		return searchHashRepository.findAll();
	}

	public SearchHash save(SearchHash entity) {
		return searchHashRepository.save(entity);
	}

	public void delete(Iterable<SearchHash> toDelete) {
		searchHashRepository.delete(toDelete);
	}
	
	public void delete(SearchHash toDelete) {
		searchHashRepository.delete(Arrays.asList(toDelete));
	}
	
	public SearchHash update(SearchHash entity) throws Exception {
		throw new UnsupportedOperationException(
				"Update of SearchHash entries is not allowed !!!");
	}

	public SearchHash updateFields(SearchHash fieldSet) throws Exception {
		throw new UnsupportedOperationException(
				"Update of SearchHash entries is not allowed !!!");
	}

	

}

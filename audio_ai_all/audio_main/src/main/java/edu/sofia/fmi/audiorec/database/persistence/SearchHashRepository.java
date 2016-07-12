package edu.sofia.fmi.audiorec.database.persistence;

import org.springframework.data.repository.CrudRepository;

import edu.sofia.fmi.audiorec.database.model.SearchHash;

public interface SearchHashRepository extends CrudRepository<SearchHash, Long> {

}

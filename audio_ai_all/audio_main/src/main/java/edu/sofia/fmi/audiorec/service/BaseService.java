package edu.sofia.fmi.audiorec.service;

import java.util.List;

public interface BaseService<T> {
	
	/**
     * Return entity from repository
     *
     * @param id
     *            - targeted entity id
     *
     * @return Entity with the required id
     */
    List<T> findById(long id);
    
	/**
     * Finds all entities in the collection.
     *
     * @return A list of entities.
     */
    List<T> findAll();

    /**
     * Stores an entity in the corresponding repository
     *
     * @param entity
     *            - entity to be stored
     *
     * @return Stored entity in the repository
     */
    T save(T entity);

    /**
     * Updates an entity in the corresponding repository
     *
     * @param entity
     *            - entity to be updated
     * @return The updated entity from the repository.
     */
    T update(T entity) throws Exception;

}

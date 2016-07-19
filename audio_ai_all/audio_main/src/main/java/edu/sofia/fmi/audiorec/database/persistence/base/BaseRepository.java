package edu.sofia.fmi.audiorec.database.persistence.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<T, R extends Serializable> extends JpaRepository<T, R> {
	
	List<T> findById(R id);
	
}

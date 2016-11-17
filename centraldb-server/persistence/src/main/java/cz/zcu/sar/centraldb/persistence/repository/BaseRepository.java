package cz.zcu.sar.centraldb.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * Created by Matej Lochman on 17.11.16.
 */

public interface BaseRepository<T, U extends Serializable> extends CrudRepository<T, U> {

}

package cz.zcu.sar.centraldb.client.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Matej Lochman on 17.11.16.
 */

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    Collection<T> findAll();
}

package cz.zcu.sar.centraldb.persistence.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Matej Lochman on 17.11.16.
 */

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID>, JpaSpecificationExecutor<T> {
    Optional<T> findOne(ID id);
    T save(T object);
    void delete(ID id);
    void delete(T object);
    void delete(Iterable<? extends T> entities);
}

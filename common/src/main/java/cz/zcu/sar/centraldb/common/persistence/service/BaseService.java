package cz.zcu.sar.centraldb.common.persistence.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 2.1.17.
 */

public interface BaseService<T, ID extends Serializable> {
    T save(T object);
    List<T> findAll();
    Optional<T> findOne(ID id);
    void delete(ID id);
    void delete(T object);
    void delete(Iterable<? extends T> entities);
}


package cz.zcu.sar.centraldb.common.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 2.1.17.
 */

public class BaseServiceImpl<T, ID extends Serializable, R extends BaseRepository<T, ID>> {

    @Autowired
    private R repository;

    public T save(T object) {
        return repository.save(object);
    }

    public List<T> findAll() {
        return repository.findAll(null);
    }

    public Optional<T> findOne(ID id) {
        return repository.findOne(id);
    }

    public void delete(ID id) {
        repository.delete(id);
    }

    public void delete(T object) {
        repository.delete(object);
    }

    public void delete(Iterable<? extends T> entities) {
        repository.delete(entities);
    }
}


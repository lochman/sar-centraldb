package cz.zcu.sar.centraldb.common.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.repository.BaseRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 2.1.17.
 */

public class BaseServiceImpl<T, ID extends Serializable, R extends BaseRepository<T, ID>> {

    @Autowired
    private R repository;

    protected final Logger logger = Logger.getLogger(this.getClass());

    public T save(T object) {
        logger.info("save: "+object.toString());
        return repository.save(object);
    }

    public List<T> findAll() {
        List<T> all = repository.findAll(null);
        logger.info("findAll: listSize=" + all.size());
        return all;
    }

    public Optional<T> findOne(ID id) {
        Optional<T> one = repository.findOne(id);
        logger.info("findOne: id=" + id + ", " + one);
        return one;
    }

    public void delete(ID id) {
        repository.delete(id);
        logger.info("delete: id=" + id);
    }

    public void delete(T object) {
        repository.delete(object);
        logger.info("delete: id=" + object);
    }

    public void delete(Iterable<? extends T> entities) {
        repository.delete(entities);
        logger.info("delete: " + entities);
    }
}


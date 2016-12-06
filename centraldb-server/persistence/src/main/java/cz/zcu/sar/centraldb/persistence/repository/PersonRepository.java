package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.persistence.domain.Person;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface PersonRepository extends BaseRepository<Person, String> {

    Optional<Person> findByName(String name);
}

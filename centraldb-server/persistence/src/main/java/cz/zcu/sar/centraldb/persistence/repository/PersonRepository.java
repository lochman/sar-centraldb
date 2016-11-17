package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.persistence.domain.Person;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface PersonRepository extends BaseRepository<Person, Long> {

    Person findByName(String name);
}

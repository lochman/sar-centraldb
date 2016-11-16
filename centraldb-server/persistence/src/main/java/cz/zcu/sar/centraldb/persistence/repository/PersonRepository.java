package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface PersonRepository extends CrudRepository<Person, Long> {

    Person findByName(String name);
}

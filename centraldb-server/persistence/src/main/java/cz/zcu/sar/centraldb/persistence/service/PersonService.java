package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.helper.PersonAddress;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 21.12.16.
 */

@Transactional
public interface PersonService {

    List<Person> getUnsynchronized(Timestamp from);
    List<Person> getUnsynchronized(Timestamp from, int count);

    Page<Person> getPeopleByQuery(PageRequestWrapper requestWrapper);

    Person savePersonAsTemp(Person person);

    Optional<Person> findOne(String id);

    Person save(Person person);
    Person createPerson(PersonAddress personAddress, String modifiedBy);
    void saveAddress(Person person, PersonAddress personAddress, String modifiedBy);
}

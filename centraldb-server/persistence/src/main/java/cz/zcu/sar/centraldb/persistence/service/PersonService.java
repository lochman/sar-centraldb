package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseService;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.wrapper.PageRequestWrapper;
import cz.zcu.sar.centraldb.persistence.wrapper.PersonAddress;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Matej Lochman on 21.12.16.
 */

@Transactional
public interface PersonService extends BaseService<Person, Long> {

    List<Person> getUnsynchronized(Timestamp from);
    List<Person> getUnsynchronized(Timestamp from, int count);

    Page<Person> getPeopleByQuery(PageRequestWrapper requestWrapper);
    Person savePersonAsTemp(Person person);
    Person createPerson(PersonAddress personAddress, String modifiedBy);
    void saveAddress(Person person, PersonAddress personAddress, String modifiedBy);
    Person findPersonByNumbers(String socialNumber, String companyNumber);
}

package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.common.persistence.service.BaseService;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 28.12.2016.
 */
public interface PersonService extends BaseService<Person, Long> {
    void createPerson(Person person);
    Person mergePerson(Person p);
    Person findPerson(Long id);
    Person findPersonByGlobalId(Long id);
    List<Person> findPersonByDate(Timestamp startDate, Timestamp endDate);
    public Person savePersonWithAddresses(Person person);
}

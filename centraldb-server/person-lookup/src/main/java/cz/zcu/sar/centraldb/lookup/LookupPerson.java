package cz.zcu.sar.centraldb.lookup;

import cz.zcu.sar.centraldb.persistence.domain.Person;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
public interface LookupPerson {
    Person findPerson(Person person);
}

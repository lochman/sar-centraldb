package cz.zcu.sar.centraldb.lookup;

import cz.zcu.sar.centraldb.persistence.domain.Person;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
public interface PersonLookup {
    /**
     * find person in db with same attribute (social or company number)
     * @param person person
     * @return persist person
     */
    Person findPerson(Person person);
}

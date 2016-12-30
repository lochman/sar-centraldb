package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;

/**
 * @author Marek Rasocha
 *         date 28.12.2016.
 */
public interface PersonService {
    public void createPerson(Person person);
}

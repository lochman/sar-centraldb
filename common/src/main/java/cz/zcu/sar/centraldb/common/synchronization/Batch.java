package cz.zcu.sar.centraldb.common.synchronization;

import cz.zcu.sar.centraldb.common.persistence.Person;

import java.util.List;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
public class Batch {
    private String clientId;
    private Person[] persons;

    public Batch(String clientId, Person[] persons) {
        this.clientId = clientId;
        this.persons = persons;
    }

    public Batch() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }
}

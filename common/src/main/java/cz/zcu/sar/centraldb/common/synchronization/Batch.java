package cz.zcu.sar.centraldb.common.synchronization;

import cz.zcu.sar.centraldb.common.persistence.Person;

import java.sql.Timestamp;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */

public class Batch {
    private Timestamp first;
    private Timestamp last;
    private String clientId;
    private Person[] persons;

    //TODO: set first, last
    public Batch(String clientId, Person[] persons) {
        this.clientId = clientId;
        this.persons = persons;
    }

    public Batch() {
    }

    public Timestamp getFirst() {
        return first;
    }

    public void setFirst(Timestamp first) {
        this.first = first;
    }

    public Timestamp getLast() {
        return last;
    }

    public void setLast(Timestamp last) {
        this.last = last;
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

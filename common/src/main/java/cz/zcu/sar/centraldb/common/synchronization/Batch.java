package cz.zcu.sar.centraldb.common.synchronization;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */

public class Batch {
    private int size;
    private String clientId;
    private PersonWrapper[] persons;

    public Batch(String clientId, PersonWrapper[] persons) {
        Arrays.sort(persons, (PersonWrapper p1, PersonWrapper p2) -> p1.getModifiedTime().compareTo(p2.getModifiedTime()));
        this.clientId = clientId;
        this.persons = persons;
    }

    public Batch() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Timestamp getFirst() {
        return persons != null && persons.length != 0 && persons[0] != null ? persons[0].getModifiedTime() : null;
    }

    public Timestamp getLast() {
        return  persons != null && persons.length != 0 && persons[persons.length - 1] != null ? persons[persons.length - 1].getModifiedTime() : null;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public PersonWrapper[] getPersons() {
        return persons;
    }

    public void setPersons(PersonWrapper[] persons) {
        this.persons = persons;
    }
}

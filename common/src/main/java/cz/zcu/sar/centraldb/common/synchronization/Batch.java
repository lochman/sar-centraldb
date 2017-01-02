package cz.zcu.sar.centraldb.common.synchronization;

import cz.zcu.sar.centraldb.common.persistence.Person;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */

public class Batch {
    private int size;
    private String clientId;
    private Person[] persons;

    public Batch(String clientId, Person[] persons) {
        Arrays.sort(persons, (Person p1, Person p2) -> p1.getModifiedTime().compareTo(p2.getModifiedTime()));
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

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }
}

package cz.zcu.sar.centraldb.persistence.helper;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;

/**
 * Created by Petr on 12/27/2016.
 */
public class PersonAddress {
    private Address addressWrappers[];
    private Person person;

    public PersonAddress(Address[] addressWrappers) {
        this.addressWrappers = addressWrappers;
    }

    public PersonAddress() {
    }

    public Address[] getAddressWrappers() {
        return addressWrappers;
    }

    public void setAddressWrappers(Address[] addressWrappers) {
        this.addressWrappers = addressWrappers;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

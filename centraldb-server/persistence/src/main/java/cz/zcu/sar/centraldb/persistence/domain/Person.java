package cz.zcu.sar.centraldb.persistence.domain;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */


@Entity
@Table(name = "person")
public class Person extends PersonWrapper<PersonType, Address> {


    private boolean temporary;
    private boolean lookupOk = true;

    public Person() {}
    public Person(String firstName, String surname, String gender) {
        super(firstName, surname, gender);
    }
    public Person(PersonWrapper person) {
        BeanUtils.copyProperties(person, this);
        // change idz
        this.setId(person.getForeignId());
        this.setForeignId(person.getId());
    }



    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public PersonWrapper getPersonWrapper() {
        PersonWrapper person = new PersonWrapper();
        BeanUtils.copyProperties(this, person);
        return person;
    }

    public boolean isLookupOk() {
        return lookupOk;
    }

    public void setLookupOk(boolean lookupOk) {
        this.lookupOk = lookupOk;
    }
}

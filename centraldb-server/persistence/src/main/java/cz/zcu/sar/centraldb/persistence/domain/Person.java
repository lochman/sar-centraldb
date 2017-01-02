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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "PersonInstitute",
        joinColumns = @JoinColumn(name = "centralId", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "instituteId")
    )
    private Set<Institute> institutes;

    private boolean temporary;

    public Person() {}
    public Person(String firstName, String surname, String gender) {
        super(firstName, surname, gender);
    }
    public Person(PersonWrapper person) {
        BeanUtils.copyProperties(person, this);
        // change id
        this.setId(person.getForeignId());
        this.setForeignId(person.getId());
    }

    public Set<Institute> getInstitutes() {
        return institutes;
    }

    public void setInstitutes(Set<Institute> institutes) {
        this.institutes = institutes;
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
}

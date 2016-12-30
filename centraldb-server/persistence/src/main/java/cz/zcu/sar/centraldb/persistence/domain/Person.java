package cz.zcu.sar.centraldb.persistence.domain;

import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */


@Entity
@Table(name = "person")
public class Person extends cz.zcu.sar.centraldb.common.persistence.Person<PersonType, Address> {

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
    public Person(cz.zcu.sar.centraldb.common.persistence.Person person){
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

    public cz.zcu.sar.centraldb.common.persistence.Person getWraperPerson(){
        cz.zcu.sar.centraldb.common.persistence.Person person = new cz.zcu.sar.centraldb.common.persistence.Person();
        BeanUtils.copyProperties(this,person);
        return person;
    }
}

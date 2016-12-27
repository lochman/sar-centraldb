package cz.zcu.sar.centraldb.persistence.domain;

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

    @Override
    public boolean equals(Object o) {
        System.out.println("mrdka z krtka");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return temporary == person.temporary &&
                Objects.equals(institutes, person.institutes);
    }

    @Override
    public int hashCode() {
        System.out.println("mrdka z krtka2");
        return Objects.hash(super.hashCode(), institutes, temporary);
    }
}

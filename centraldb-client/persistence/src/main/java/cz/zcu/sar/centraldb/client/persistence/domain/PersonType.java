package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class PersonType extends BaseObject {

    @Column(length = 10)
    private String personType;

    @OneToMany(mappedBy = "personType")
    private Set<Person> people;

    private String description;

    public String getPersonType() { return personType; }

    public void setPersonType(String personType) { this.personType = personType; }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}

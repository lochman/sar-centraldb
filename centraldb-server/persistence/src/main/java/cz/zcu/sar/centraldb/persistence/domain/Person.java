package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */


@Entity
@Table(name = "person")
public class Person extends wrapper.Person<PersonType,Address> {



    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "PersonInstitute",
        joinColumns = @JoinColumn(name = "centralId", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "instituteId")
    )
    private Set<Institute> institutes;

    private boolean temporary;

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
}

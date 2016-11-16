package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Person extends BaseObject {

//    @Id
//    private String centralId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private PersonType personType;

    @OneToMany(mappedBy = "person")
    private Set<Address> addresses;

//    public String getCentralId() {
//        return centralId;
//    }
//
//    public void setCentralId(String centralId) {
//        this.centralId = centralId;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }
}

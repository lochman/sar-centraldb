package cz.zcu.sar.centraldb.client.persistence.domain;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
@Table(name = "person")
public class Person extends PersonWrapper<PersonType, Address> {

   private Long centralId;

    public Long getCentralId() {
        return centralId;
    }

    public void setCentralId(Long centralId) {
        this.centralId = centralId;
    }
    public PersonWrapper getWraperPerson() {
        PersonWrapper person = new PersonWrapper();
        BeanUtils.copyProperties(this,person);
        person.setForeignId(getCentralId());
        return person;
    }
    public Person(PersonWrapper<PersonTypeWrapper, AddressWrapper> person){
        BeanUtils.copyProperties(person, this);
        // change id
        Set<Address> addresses= new HashSet<>();
        if (person.getAddressWrappers()==null) person.setAddressWrappers(new HashSet<>());
        for (AddressWrapper a : person.getAddressWrappers()){
            Address add = new cz.zcu.sar.centraldb.client.persistence.domain.Address(a);
            add.setPerson(this);
            addresses.add(add);
        }
        this.setAddressWrappers(addresses);
        this.setPersonType(new PersonType(person.getPersonType()));
        this.setId(person.getForeignId());
        this.setForeignId(person.getId());
    }

    public Person() {
        super();
    }
    public Person(String firstName, String name, String gender) {
        super(firstName,name,gender);
    }
}

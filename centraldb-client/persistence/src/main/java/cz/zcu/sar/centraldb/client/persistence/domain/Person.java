package cz.zcu.sar.centraldb.client.persistence.domain;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
@Table(name = "person")
public class Person extends cz.zcu.sar.centraldb.common.persistence.Person<PersonType, Address> {

   private Long centralId;

    public Long getCentralId() {
        return centralId;
    }

    public void setCentralId(Long centralId) {
        this.centralId = centralId;
    }
    public cz.zcu.sar.centraldb.common.persistence.Person getWraperPerson(){
        cz.zcu.sar.centraldb.common.persistence.Person person = new cz.zcu.sar.centraldb.common.persistence.Person();
        BeanUtils.copyProperties(this,person);
        person.setForeignId(getCentralId());
        return person;
    }
    public Person(cz.zcu.sar.centraldb.common.persistence.Person< cz.zcu.sar.centraldb.common.persistence.PersonType, cz.zcu.sar.centraldb.common.persistence.Address> person){
        BeanUtils.copyProperties(person,this);
        // change id
        Set<Address> addresses= new HashSet<>();
        if (person.getAddressWrappers()==null) person.setAddressWrappers(new HashSet<>());
        for (cz.zcu.sar.centraldb.common.persistence.Address a : person.getAddressWrappers()){
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

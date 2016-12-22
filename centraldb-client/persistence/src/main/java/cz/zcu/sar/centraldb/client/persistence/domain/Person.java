package cz.zcu.sar.centraldb.client.persistence.domain;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

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
//        person.setAddressWrappers(null);
//        person.setPersonType(null);
        return person;
    }
    public Person(cz.zcu.sar.centraldb.common.persistence.Person person){
        BeanUtils.copyProperties(person,this);
        // change id
        this.setId(person.getForeignId());
        this.setForeignId(person.getId());
    }

    public Person() {
    }
}

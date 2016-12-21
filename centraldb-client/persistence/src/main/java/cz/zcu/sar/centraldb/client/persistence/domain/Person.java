package cz.zcu.sar.centraldb.client.persistence.domain;

import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
@Table(name = "person")
public class Person extends wrapper.Person<PersonType, Address> {

   private Long centralId;

    public Long getCentralId() {
        return centralId;
    }

    public void setCentralId(Long centralId) {
        this.centralId = centralId;
    }
    public wrapper.Person getWraperPerson(){
        wrapper.Person person = new wrapper.Person();
        BeanUtils.copyProperties(person, this);
        person.setForeignId(getCentralId());
        return person;
    }

}

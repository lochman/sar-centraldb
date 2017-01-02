package cz.zcu.sar.centraldb.client.persistence.domain;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class PersonType extends PersonTypeWrapper {
    public PersonType(PersonTypeWrapper personType){
        super();
        BeanUtils.copyProperties(personType, this);
    }

    public PersonType() {
    }
}

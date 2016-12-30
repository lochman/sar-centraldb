package cz.zcu.sar.centraldb.client.persistence.domain;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class PersonType extends cz.zcu.sar.centraldb.common.persistence.PersonType {
    public PersonType(cz.zcu.sar.centraldb.common.persistence.PersonType personType){
        super();
        BeanUtils.copyProperties(personType, this);
    }

    public PersonType() {
    }
}

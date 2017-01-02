package cz.zcu.sar.centraldb.client.persistence.domain;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressTypeWrapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class AddressType extends AddressTypeWrapper {
    public AddressType(AddressTypeWrapper addressType){
        super();
        BeanUtils.copyProperties(addressType, this);
    }

    public AddressType() {
    }
}

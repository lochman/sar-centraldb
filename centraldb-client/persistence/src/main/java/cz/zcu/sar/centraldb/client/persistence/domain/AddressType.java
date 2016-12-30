package cz.zcu.sar.centraldb.client.persistence.domain;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class AddressType extends cz.zcu.sar.centraldb.common.persistence.AddressType {
    public AddressType(cz.zcu.sar.centraldb.common.persistence.AddressType addressType){
        super();
        BeanUtils.copyProperties(addressType, this);
    }

    public AddressType() {
    }
}

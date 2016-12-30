package cz.zcu.sar.centraldb.client.persistence.domain;

import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Address extends cz.zcu.sar.centraldb.common.persistence.Address<Person,AddressType> {

    public Address() {
    }

    public Address(cz.zcu.sar.centraldb.common.persistence.Address address){
        super();
        BeanUtils.copyProperties(address, this);
        this.setAddressType(new AddressType(this.addressType));
    }
    @Override
    public boolean equals(Object o) {
      return super.equals(o);
    }
}

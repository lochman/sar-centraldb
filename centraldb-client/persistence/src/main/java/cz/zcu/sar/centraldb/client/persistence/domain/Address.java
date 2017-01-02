package cz.zcu.sar.centraldb.client.persistence.domain;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Address extends AddressWrapper<Person, AddressType> {

    public Address() {
    }

    public Address(AddressWrapper address){
        super();
        BeanUtils.copyProperties(address, this);
        this.setAddressType(new AddressType(this.addressType));
    }
    @Override
    public boolean equals(Object o) {
      return super.equals(o);
    }
}

package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Address extends wrapper.Address<Person,AddressType> {

}

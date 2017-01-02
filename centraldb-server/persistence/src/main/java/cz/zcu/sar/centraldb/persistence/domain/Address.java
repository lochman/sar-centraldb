package cz.zcu.sar.centraldb.persistence.domain;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Address extends AddressWrapper<Person, AddressType> {

}

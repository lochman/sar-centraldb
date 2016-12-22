package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.Entity;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Address extends cz.zcu.sar.centraldb.common.persistence.Address<Person,AddressType> {
}

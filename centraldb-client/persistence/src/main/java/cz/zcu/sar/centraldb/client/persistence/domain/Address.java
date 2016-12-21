package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Address extends cz.zcu.sar.centraldb.common.persistence.Address<Person,AddressType> {
}

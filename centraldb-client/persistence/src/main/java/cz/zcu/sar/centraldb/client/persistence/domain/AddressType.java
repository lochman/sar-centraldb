package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class AddressType extends wrapper.AddressType<Address> {

}

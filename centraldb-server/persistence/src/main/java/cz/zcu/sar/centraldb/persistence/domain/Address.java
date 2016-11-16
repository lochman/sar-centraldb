package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Address extends BaseObject {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Person person;

    @Column(nullable = false)
    private AddressType addressType;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }
}

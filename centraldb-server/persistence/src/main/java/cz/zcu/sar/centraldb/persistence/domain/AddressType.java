package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class AddressType extends BaseObject {

    @OneToMany(mappedBy = "addressType")
    private Set<Address> addresses;

    private String description;

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}

package wrapper;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */
@MappedSuperclass
public class AddressType<T> extends BaseObject {


    @Column(length = 10)
    protected String addressType;

    @OneToMany(mappedBy = "addressType")
    protected Set<T> addresses;

    protected String description;

    public String getAddressType() { return addressType; }

    public void setAddressType(String addressType) { this.addressType = addressType; }

    public Set<T> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<T> addresses) {
        this.addresses = addresses;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}

package cz.zcu.sar.centraldb.common.persistence.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by Matej Lochman on 31.10.16.
 */
@MappedSuperclass
public class AddressTypeWrapper extends BaseObject {

    @Column(length = 10)
    private String addressType;

    private String description;

    public String getAddressType() { return addressType; }

    public void setAddressType(String addressType) { this.addressType = addressType; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "AddressTypeWrapper{" +
                "id=" + getId() +
                ", addressType='" + addressType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

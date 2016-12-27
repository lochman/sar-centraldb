package cz.zcu.sar.centraldb.common.persistence;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public class Address<P extends Person,A extends AddressType> extends BaseObject {

    @ManyToOne
    @JoinColumn(nullable = false)
    protected P person;

    @ManyToOne
    @JoinColumn(nullable = false)
    protected A addressType;

    protected Date residenceFrom;
    protected Date residenceTo;

    protected String city;
    protected String street;

    @Column(length = 10)
    protected String land_registry_number;
    protected String country;

    @Column(length = 4)
    protected String countryCode;

    @Column(length = 30)
    protected String postalCode;

    @Column(length = 30)
    protected String phone;

    @Column(length = 30)
    protected String fax;

    @Column(length = 20)
    protected String district;
    protected String flatAt;
    protected String borough;

    public P getPerson() {
        return person;
    }

    public void setPerson(P person) {
        this.person = person;
    }

    public A getAddressType() {
        return addressType;
    }

    public void setAddressType(A addressType) {
        this.addressType = addressType;
    }

    public Date getResidenceFrom() {
        return residenceFrom;
    }

    public void setResidenceFrom(Date residenceFrom) {
        this.residenceFrom = residenceFrom;
    }

    public Date getResidenceTo() {
        return residenceTo;
    }

    public void setResidenceTo(Date residenceTo) {
        this.residenceTo = residenceTo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLand_registry_number() {
        return land_registry_number;
    }

    public void setLand_registry_number(String land_registry_number) {
        this.land_registry_number = land_registry_number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFlatAt() {
        return flatAt;
    }

    public void setFlatAt(String flatAt) {
        this.flatAt = flatAt;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }
}

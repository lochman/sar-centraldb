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
public class Address extends BaseObject {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Person person;

    @Column(nullable = false)
    @ManyToOne
    private AddressType addressType;

    private Date residenceFrom;
    private Date residenceTo;

    private String city;
    private String street;

    @Column(length = 10)
    private String land_registry_number;
    private String country;

    @Column(length = 4)
    private String countryCode;

    @Column(length = 30)
    private String postalCode;

    @Column(length = 30)
    private String phone;

    @Column(length = 30)
    private String fax;

    @Column(length = 20)
    private String district;
    private String flatAt;
    private String borough;

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

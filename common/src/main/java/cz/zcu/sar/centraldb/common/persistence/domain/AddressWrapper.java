package cz.zcu.sar.centraldb.common.persistence.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public class AddressWrapper<P extends PersonWrapper, A extends AddressTypeWrapper> extends BaseObject {

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonBackReference
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
       // if (!super.equals(object)) return false;

        AddressWrapper<?, ?> address = (AddressWrapper<?, ?>) object;
        if (addressType != null ? !addressType.equals(address.addressType) : address.addressType != null) return false;
        if (residenceFrom != null ? !residenceFrom.equals(address.residenceFrom) : address.residenceFrom != null)
            return false;
        if (residenceTo != null ? !residenceTo.equals(address.residenceTo) : address.residenceTo != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (land_registry_number != null ? !land_registry_number.equals(address.land_registry_number) : address.land_registry_number != null)
            return false;
        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        if (countryCode != null ? !countryCode.equals(address.countryCode) : address.countryCode != null) return false;
        if (postalCode != null ? !postalCode.equals(address.postalCode) : address.postalCode != null) return false;
        if (phone != null ? !phone.equals(address.phone) : address.phone != null) return false;
        if (fax != null ? !fax.equals(address.fax) : address.fax != null) return false;
        if (district != null ? !district.equals(address.district) : address.district != null) return false;
        if (flatAt != null ? !flatAt.equals(address.flatAt) : address.flatAt != null) return false;
        return !(borough != null ? !borough.equals(address.borough) : address.borough != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0);
        result = 31 * result + (residenceFrom != null ? residenceFrom.hashCode() : 0);
        result = 31 * result + (residenceTo != null ? residenceTo.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (land_registry_number != null ? land_registry_number.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (flatAt != null ? flatAt.hashCode() : 0);
        result = 31 * result + (borough != null ? borough.hashCode() : 0);
        return result;
    }
    public String toString() {
        return "AddressWrapper{" +
                "id=" + getId() +
                ", person=" + person.getId() +
                ", addressType=" + addressType +
                ", residenceFrom=" + residenceFrom +
                ", residenceTo=" + residenceTo +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", land_registry_number='" + land_registry_number + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", district='" + district + '\'' +
                ", flatAt='" + flatAt + '\'' +
                ", borough='" + borough + '\'' +
                '}';
    }
}

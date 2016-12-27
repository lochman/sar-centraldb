package cz.zcu.sar.centraldb.common.persistence;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public class Person<P extends PersonType, A extends Address> extends BaseObject {

    @Transient
    private Long foreignId;

    @ManyToOne
    @JoinColumn(nullable = true)
    private P personType;

    @OneToMany(mappedBy = "person")
    @JsonManagedReference
    private Set<A> addressWrappers;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, length = 40)
    private String firstName;

    @Column(nullable = true, length = 40)
    private String birthName;

    @Column(nullable = true, length = 50)
    private String birthplace;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = true, length = 1)
    private String gender;

    @Column(length = 50)
    private String degreeBefore;

    @Column(length = 50)
    private String degreeAfter;

    @Column(length = 20)
    private String supervisor;

    @Column(length = 1000)
    private String description;

    @Column(nullable = true, length = 20)
    private String citizenship;

    @Column(length = 9)
    private String companyNumber;

    @Column(nullable = true, length = 15)
    private String socialNumber;

    @Column(length = 1)
    private String usePermitted;

    public Person() { }

    public Person(String firstName, String name, String gender) {
        this.firstName = firstName;
        this.name = name;
        this.birthName = name;
        this.gender = gender;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public void setForeignId(Long foreignId) {
        this.foreignId = foreignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public P getPersonType() {
        return personType;
    }

    public void setPersonType(P personType) {
        this.personType = personType;
    }

    public Set<A> getAddressWrappers() {
        return addressWrappers;
    }

    public void setAddressWrappers(Set<A> addressWrappers) {
        this.addressWrappers = addressWrappers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBirthName() {
        return birthName;
    }

    public void setBirthName(String birthName) {
        this.birthName = birthName;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDegreeBefore() {
        return degreeBefore;
    }

    public void setDegreeBefore(String degreeBefore) {
        this.degreeBefore = degreeBefore;
    }

    public String getDegreeAfter() {
        return degreeAfter;
    }

    public void setDegreeAfter(String degreeAfter) {
        this.degreeAfter = degreeAfter;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(String socialNumber) {
        this.socialNumber = socialNumber;
    }

    public String getUsePermitted() {
        return usePermitted;
    }

    public void setUsePermitted(String usePermitted) {
        this.usePermitted = usePermitted;
    }
}

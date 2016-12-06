package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class Person extends BaseObject {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private PersonType personType;

    @OneToMany(mappedBy = "person")
    private Set<Address> addresses;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "PersonInstitute",
        joinColumns = @JoinColumn(name = "centralId", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "instituteId")
    )
    private Set<Institute> institutes;

    private boolean temporary;

    @Column(length = 40)//, nullable = false)
    private String firstName;

    @Column(length = 40)//, nullable = false)
    private String birthName;

    @Column(length = 50)//, nullable = false)
    private String birthplace;

    //@Column(nullable = false)
    private Date birthDate;

    @Column(length = 1)//, nullable = false)
    private String gender;

    @Column(length = 50)
    private String degreeBefore;

    @Column(length = 50)
    private String degreeAfter;

    @Column(length = 20)
    private String supervisor;

    @Column(length = 1000)
    private String description;

    @Column(length = 20)//, nullable = false)
    private String citizenship;

    @Column(length = 9)
    private String companyNumber;

    @Column(length = 15)//, nullable = false)
    private String socialNumber;

    @Column(length = 1)
    private String usePermitted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Institute> getInstitutes() {
        return institutes;
    }

    public void setInstitutes(Set<Institute> institutes) {
        this.institutes = institutes;
    }

    public boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
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

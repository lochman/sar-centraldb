package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.*;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
public class PersonInstitute {
    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institute_id")
    private Institute institute;
    private Long localId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

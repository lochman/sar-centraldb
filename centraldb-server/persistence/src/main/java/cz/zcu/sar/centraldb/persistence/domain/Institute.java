package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created by Matej Lochman on 17.11.16.
 */


@Entity
public class Institute {

    @Id
    private Long institute;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "institutes", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Person> people;

    private String description;

    private String lastBatchId;
    private Timestamp lastSyncIn;
    private Timestamp lastSyncOut;

    public Long getInstitute() {
        return institute;
    }

    public void setInstitute(Long institute) {
        this.institute = institute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastBatchId() {
        return lastBatchId;
    }

    public void setLastBatchId(String lastBatchId) {
        this.lastBatchId = lastBatchId;
    }

    public Timestamp getLastSyncIn() {
        return lastSyncIn;
    }

    public void setLastSyncIn(Timestamp lastSyncIn) {
        this.lastSyncIn = lastSyncIn;
    }

    public Timestamp getLastSyncOut() {
        return lastSyncOut;
    }

    public void setLastSyncOut(Timestamp lastSyncOut) {
        this.lastSyncOut = lastSyncOut;
    }
}

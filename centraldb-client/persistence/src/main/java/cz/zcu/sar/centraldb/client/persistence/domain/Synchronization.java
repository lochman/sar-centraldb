package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Matej Lochman on 17.11.16.
 */

@Entity
public class Synchronization {

    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp fistDate;
    private Timestamp lastDate;
    @Column(length = 50)
    private String batchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFistDate() {
        return fistDate;
    }

    public void setFistDate(Timestamp fistDate) {
        this.fistDate = fistDate;
    }

    public Timestamp getLastDate() {
        return lastDate;
    }

    public void setLastDate(Timestamp lastDate) {
        this.lastDate = lastDate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Synchronization  that = (Synchronization) o;

        return id.equals(that.id);

    }
}

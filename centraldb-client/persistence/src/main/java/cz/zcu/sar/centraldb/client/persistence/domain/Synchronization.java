package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by Matej Lochman on 17.11.16.
 */

@Entity
public class Synchronization {

    @Id
    private Long id;

    private Timestamp syncPointer;

    @Column(length = 50)
    private String batchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getSyncPointer() {
        return syncPointer;
    }

    public void setSyncPointer(Timestamp syncPointer) {
        this.syncPointer = syncPointer;
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

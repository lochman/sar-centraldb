package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

/**
 * Created by Matej Lochman on 17.11.16.
 */

@Entity
public class Synchronization {

    private Timestamp syncPointer;

    @Column(length = 50)
    private String batchId;

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
}

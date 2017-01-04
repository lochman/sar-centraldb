package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by Matej Lochman on 17.11.16.
 */

@Entity
public class Institute {

    @Id
    private Long id;
    private String name;
    private String description;
    private String lastBatchId;
    private Timestamp lastSyncIn;
    private Timestamp lastSyncOut;

    public Institute() {
    }

    public Institute(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

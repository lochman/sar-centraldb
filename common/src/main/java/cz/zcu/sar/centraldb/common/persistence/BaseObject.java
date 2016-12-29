package cz.zcu.sar.centraldb.common.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public abstract class BaseObject implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 20)
    private String modifiedBy;

    @Column(nullable = false)
    private Timestamp modifiedTime;

    @PreUpdate
    @PrePersist
    public void setModificationTimeStamp() {
        modifiedTime = new Timestamp(new Date().getTime());
    }

    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Timestamp getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Timestamp modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseObject that = (BaseObject) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

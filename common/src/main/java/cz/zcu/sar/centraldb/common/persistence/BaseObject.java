package cz.zcu.sar.centraldb.common.persistence;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public class BaseObject implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 20)
    private String modifiedBy;

    @Column(nullable = false)
    private Timestamp modifiedTime;

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
        return id.hashCode();
    }
}

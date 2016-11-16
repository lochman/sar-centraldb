package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public class BaseObject implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    public boolean isNew() {
        return id == null;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

package cz.zcu.sar.centraldb.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
public class PersonType extends BaseObject {

    @Id
    @Column(length = 10)
    private String type;

    private String description;

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}

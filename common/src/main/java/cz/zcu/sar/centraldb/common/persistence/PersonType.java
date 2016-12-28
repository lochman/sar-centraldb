package cz.zcu.sar.centraldb.common.persistence;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public class PersonType extends BaseObject {

    @Column(length = 10)
    protected String personType;

    protected String description;

    public String getPersonType() { return personType; }

    public void setPersonType(String personType) { this.personType = personType; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }



}

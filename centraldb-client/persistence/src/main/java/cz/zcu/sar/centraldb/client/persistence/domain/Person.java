package cz.zcu.sar.centraldb.client.persistence.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@Entity
@Table(name = "person")
public class Person extends wrapper.Person<PersonType, Address> {

    @Column(length = 20)
    private String centralId;

    public String getCentralId() {
        return centralId;
    }

    public void setCentralId(String centralId) {
        this.centralId = centralId;
    }

}

package wrapper;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@MappedSuperclass
public class PersonType<P extends Person> extends BaseObject {

    @Column(length = 10)
    protected String personType;

    @OneToMany(mappedBy = "personType")
    protected Set<P> people;

    protected String description;

    public String getPersonType() { return personType; }

    public void setPersonType(String personType) { this.personType = personType; }

    public Set<P> getPeople() {
        return people;
    }

    public void setPeople(Set<P> people) {
        this.people = people;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}

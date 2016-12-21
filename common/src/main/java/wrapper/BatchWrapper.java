package wrapper;

import java.util.List;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
public class BatchWrapper {
    private String clientId;
    private List<Person> persons;

    public BatchWrapper(String clientId, List<Person> persons) {
        this.clientId = clientId;
        this.persons=persons;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

}

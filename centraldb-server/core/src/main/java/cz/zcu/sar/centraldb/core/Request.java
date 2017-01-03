package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.persistence.domain.Person;

import java.util.List;

/**
 * Created by Matej Lochman on 3.1.17.
 */
public class Request {
    private String batchId;
    private String clientId;
    private List<Person> people;

    public Request() {

    }

    public Request(String batchId, String clientId) {
        this.batchId = batchId;
        this.clientId = clientId;
    }

    public Request(String batchId, String clientId, List<Person> people) {
        this.batchId = batchId;
        this.clientId = clientId;
        this.people = people;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (batchId != null ? !batchId.equals(request.batchId) : request.batchId != null) return false;
        return clientId != null ? clientId.equals(request.clientId) : request.clientId == null;
    }

    @Override
    public int hashCode() {
        int result = batchId != null ? batchId.hashCode() : 0;
        result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
        return result;
    }
}

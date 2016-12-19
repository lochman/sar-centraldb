package cz.zcu.sar.centraldb.client.rest;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;

import java.util.List;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */
public interface Sender {
    public boolean sendLastBatchId(String batchId);
    public void sendData(List<Person> persons,String batchId);
}

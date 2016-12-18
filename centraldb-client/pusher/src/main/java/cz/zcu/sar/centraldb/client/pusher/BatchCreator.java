package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 18.12.2016.
 */

public interface BatchCreator {
    /**
     * create batch betwen start and end date. End date is countig while Batch dont have  a full buffer
     * full buffer end send. If dont have data, return emty list
     * @param start start date
     * @return list
     */
    public List<Person> createBatch(Timestamp start);
    public List<Person> createBatch(Timestamp start,Timestamp end);

    public Timestamp getEndDate();
    public Timestamp getStartDate();
}

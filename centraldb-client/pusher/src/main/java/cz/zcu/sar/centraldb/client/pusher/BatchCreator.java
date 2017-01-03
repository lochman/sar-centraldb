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
     * create batch from start date. End date is counting while Batch dont have  a full buffer
     * return empty list
     * @param start start date
     * @return list
     */
    List<Person> createBatch(Timestamp start);
    /**
     * create batch between start and end date.
     * return empty list
     * @param start start date
     * @return list
     */
    List<Person> createBatch(Timestamp start,Timestamp end);

    Timestamp getEndDate();
    Timestamp getStartDate();
}

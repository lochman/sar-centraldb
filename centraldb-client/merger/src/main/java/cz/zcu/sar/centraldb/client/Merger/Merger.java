package cz.zcu.sar.centraldb.client.Merger;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;

import java.util.List;

/**
 * Merge client person and server person
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
public interface Merger {
    /**
     * merge person from server (SP) and client person(CP)
     * CP is search - 1) localId that can be save in server
     *                2] globalId that is primary key in server side
     *                3) search by social a company number
     * if CP isnt find, SP is save as a new person
     * otherwise is SP and CP is merge.
     *          * Address is join from SP and CP(duplicities are removed).
     *              * duplicities is discerned by <code>equals</code>
     *          * Others attributes is filled from SP
     * @param persons persons to merge
     * @return merge completed
     */
    boolean mergeData(List<Person> persons);
}

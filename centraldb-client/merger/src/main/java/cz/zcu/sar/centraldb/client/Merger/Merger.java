package cz.zcu.sar.centraldb.client.Merger;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;

import java.util.List;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
public interface Merger {
    public boolean mergeData(List<Person> persons, String batchId);
}

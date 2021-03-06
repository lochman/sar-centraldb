package cz.zcu.sar.centraldb.synchronization;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.persistence.domain.Person;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * Created by Matej Lochman on 23.12.16.
 */

public interface SyncQueue {
    void initQueue();
    boolean pushData(List<Person> data, Long instituteId);
    Collection<PersonWrapper> pullData(Long instituteId, int size);
    boolean updateLastSync(Long instituteId, Timestamp lastSync);
}

package cz.zcu.sar.centraldb.synchronization;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Matej Lochman on 28.12.16.
 */

public class SeparateSyncQueue implements SyncQueue {

    @Autowired
    private PersonService personService;

    @Autowired
    private InstituteService instituteService;

    private Map<Long, PriorityQueue<PersonWrapper>> queues;

    public SeparateSyncQueue() {
        initQueue();
    }

    private void initQueue() {
        List<Institute> institutes = instituteService.findAll();
        List<Person> unSynchronized;
        queues = new HashMap<>();
        for (Institute institute : institutes) {
            queues.put(institute.getInstitute(), new PriorityQueue<>((PersonWrapper p1, PersonWrapper p2) -> p1.getModifiedTime().compareTo(p2.getModifiedTime())));
            unSynchronized = personService.getUnsynchronized(institute.getLastSyncOut());
            if (!unSynchronized.isEmpty()) {
                queues.get(institute.getInstitute()).addAll(unSynchronized);
            }
        }
    }

    @Override
    public boolean pushData(Collection<Person> data, Long instituteId) {
        Timestamp lastSyncTime = queues.get(instituteId).peek().getModifiedTime();
        for (Person person : data) {
            if (person.getModifiedTime().before(lastSyncTime)) {
                instituteService.updateSyncOut(instituteId, person.getModifiedTime());
            }
            queues.get(instituteId).add(person.getPersonWrapper());
        }
        return true;
    }

    @Override
    public Collection<PersonWrapper> pullData(Long instituteId, int size) {
        PriorityQueue<PersonWrapper> queue = queues.get(instituteId);
        size = size < queue.size() ? size : queue.size();
        return Arrays.asList(queue.toArray(new PersonWrapper[0])).subList(0, size);
    }

    @Override
    public boolean updateLastSync(Long instituteId, Timestamp lastSync) {
        PriorityQueue<PersonWrapper> queue = queues.get(instituteId);
        PersonWrapper person = queue.poll();
        while (!queue.isEmpty() && person != null && person.getModifiedTime() != lastSync) {
            person = queue.poll();
        }
        instituteService.updateSyncOut(instituteId, lastSync);
        return Objects.equals(queue.peek().getModifiedTime(), lastSync);
    }
}

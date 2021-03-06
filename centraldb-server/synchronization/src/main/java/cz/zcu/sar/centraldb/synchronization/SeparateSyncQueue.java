package cz.zcu.sar.centraldb.synchronization;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Matej Lochman on 28.12.16.
 */

@Primary
@Component
public class SeparateSyncQueue implements SyncQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeparateSyncQueue.class);
    @Autowired
    private PersonService personService;
    @Autowired
    private InstituteService instituteService;

    private Map<Long, PriorityQueue<PersonWrapper>> queues;

    public SeparateSyncQueue() { }

    @Override
    public void initQueue() {
        List<Institute> institutes = instituteService.findAll();
        List<PersonWrapper> wholeDB = personService.getUnsynchronized(null);
        List<PersonWrapper> unSynchronized;
        Timestamp time;
        queues = new HashMap<>();
        for (Institute institute : institutes) {
            queues.put(institute.getId(), new PriorityQueue<>((PersonWrapper p1, PersonWrapper p2) -> p1.getModifiedTime().compareTo(p2.getModifiedTime())));
            time = institute.getLastSyncOut();
            if (time == null) {
                unSynchronized = new ArrayList<>(wholeDB.size());
                unSynchronized.addAll(wholeDB);
            } else {
                unSynchronized = personService.getUnsynchronized(time);
            }
            if (!unSynchronized.isEmpty()) {
                queues.get(institute.getId()).addAll(unSynchronized);
                LOGGER.info("SyncQueue: loaded {} records from DB for institute {}", unSynchronized.size(), institute.getId());
            }
        }
        LOGGER.info("Synchronization queue initialized");
    }

    private void checkModifiedTime(Person person, Long instituteId) {
        Timestamp time = person.getModifiedTime();
        if (time == null || !queues.containsKey(instituteId)) {
            return;
        }
        PersonWrapper personWrapper = queues.get(instituteId).peek();
        if (personWrapper != null) {
            Timestamp lastModified = personWrapper.getModifiedTime();
            if (lastModified == null || time.before(lastModified)) {
                instituteService.updateSyncOut(instituteId, time);
            }
        }
    }

    @Override
    public boolean pushData(List<Person> data, Long instituteId) {
        if (data.isEmpty()) {
            return false;
        }
        for (Map.Entry<Long, PriorityQueue<PersonWrapper>> entry : queues.entrySet()) {
            if (Objects.equals(entry.getKey(), instituteId)) {
                continue;
            }
            checkModifiedTime(data.get(0), entry.getKey());
            for (Person person : data) {
                entry.getValue().add(person.getPersonWrapper());
            }
        }
        return true;
    }

    @Override
    public Collection<PersonWrapper> pullData(Long instituteId, int size) {
        PriorityQueue<PersonWrapper> queue = queues.get(instituteId);
//        LOGGER.debug("SyncQueue: pull queue is {}, instituteID is {}", queue, instituteId);
        if (queue == null) {
            return new LinkedList<>();
        }
        LOGGER.debug("SyncQueue: pull of size {}, queue size is {}", size, queue.size());
        return Arrays.asList(queue.toArray(new PersonWrapper[0])).subList(0, Math.min(size, queue.size()));
    }

    @Override
    public boolean updateLastSync(Long instituteId, Timestamp lastSync) {
        PriorityQueue<PersonWrapper> queue = queues.get(instituteId);
        PersonWrapper person = queue.poll();
        while (!queue.isEmpty() && person != null && !Objects.equals(person.getModifiedTime(), lastSync)) {
            person = queue.poll();
        }
        instituteService.updateSyncOut(instituteId, lastSync);
        return queue.isEmpty() || Objects.equals(queue.peek().getModifiedTime(), lastSync);
    }
}

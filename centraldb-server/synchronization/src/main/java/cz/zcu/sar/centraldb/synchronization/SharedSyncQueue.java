package cz.zcu.sar.centraldb.synchronization;

import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Matej Lochman on 23.12.16.
 */

@Component
public class SharedSyncQueue implements SyncQueue {

    @Autowired
    private PersonService personService;

    @Autowired
    private InstituteService instituteService;

    private TreeSet<Person> queue;
    private Map<Long, Timestamp> lastSync;
    private Map<Timestamp, List<Person>> peopleByTime;

    public SharedSyncQueue() {
        //initQueue();
    }
    @PostConstruct
    private void initQueue() {
        List<Institute> institutes = instituteService.findAll();
        List<Person> unSynchronized;
        lastSync = new HashMap<>();
        queue = new TreeSet<>((Person p1, Person p2) -> p1.getModifiedTime().compareTo(p2.getModifiedTime()));
        for (Institute institute : institutes) {
            unSynchronized = personService.getUnsynchronized(institute.getLastSyncOut());
            if (!unSynchronized.isEmpty()) {
                queue.addAll(unSynchronized);
                lastSync.put(institute.getInstitute(), unSynchronized.get(0).getModifiedTime());
                for (Person person : unSynchronized) {
                    addPersonToMap(person);
                }
            }
        }
    }

    private void addPersonToMap(Person person) {
        if (!peopleByTime.containsKey(person.getModifiedTime())) {
            peopleByTime.put(person.getModifiedTime(), new LinkedList<>());
        }
        peopleByTime.get(person.getModifiedTime()).add(person);
    }

    private void removePersonFromMap(Person person) {
        List<Person> people = peopleByTime.get(person.getModifiedTime());
        people.remove(person);
        if (people.isEmpty()) {
            peopleByTime.remove(person.getModifiedTime());
        }
    }

    @Override
    public boolean pushData(Collection<Person> data, Long instituteId) {
        Timestamp lastSyncTime = lastSync.get(instituteId);
        for (Person person : data) {
            if (person.getModifiedTime().before(lastSyncTime)) {
                lastSync.put(instituteId, person.getModifiedTime());
                instituteService.updateSyncOut(instituteId, person.getModifiedTime());
            }
            queue.add(person);
            addPersonToMap(person);
        }
        return true;
    }

    @Override
    public Collection<Person> pullData(Long instituteId, int size) {
        List<Person> data = new LinkedList<>();
        Timestamp lastSyncTime = lastSync.get(instituteId);
        Person from = null;
        Iterator<Person> iterator = queue.iterator();
        while (iterator.hasNext()) {
            from = iterator.next();
            if (from.getModifiedTime() == lastSyncTime) {
                from = iterator.next();
                break;
            }
        }
        if(from!=null){
            data.addAll(queue.tailSet(from));//TODO sublist
        }
        return data;
    }

    private boolean isLastToSync(Long instituteId) {
        return Objects.equals(lastSyncTime(), lastSync.get(instituteId));
    }

    private Timestamp lastSyncTime() {
        return Collections.min(lastSync.values());
    }

    private void reduceQueue() {
        Person person;
        Iterator<Person> iterator = queue.iterator();
        while (iterator.hasNext()) {
            person = iterator.next();
            iterator.remove();
            removePersonFromMap(person);
            if (person.getModifiedTime() == lastSyncTime()) {
                break;
            }
        }
    }

    @Override
    public boolean updateLastSync(Long instituteId, Timestamp syncTime) {
        if (isLastToSync(instituteId)) {
            reduceQueue();
        }
        lastSync.remove(instituteId);
        lastSync.put(instituteId, syncTime);
        instituteService.updateSyncOut(instituteId, syncTime);
        return false;
    }
}

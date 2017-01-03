package cz.zcu.sar.centraldb.synchronization;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
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

    private TreeSet<PersonWrapper> queue;
    private Map<Long, Timestamp> lastSync;
    private Map<Timestamp, List<PersonWrapper>> peopleByTime;

    public SharedSyncQueue() {

    }

    @PostConstruct
    private void initQueue() {
        List<Institute> institutes = instituteService.findAll();
        List<Person> unSynchronized;
        lastSync = new HashMap<>();
        peopleByTime = new HashMap<>();
        queue = new TreeSet<>((PersonWrapper p1, PersonWrapper p2) -> p1.getModifiedTime().compareTo(p2.getModifiedTime()));
        for (Institute institute : institutes) {
            unSynchronized = personService.getUnsynchronized(institute.getLastSyncOut());
            if (!unSynchronized.isEmpty()) {
                queue.addAll(unSynchronized);
                lastSync.put(institute.getId(), unSynchronized.get(0).getModifiedTime());
                for (Person person : unSynchronized) {
                    addPersonToMap(person);
                }
            }
        }
    }

    private void addPersonToMap(PersonWrapper person) {
        if (!peopleByTime.containsKey(person.getModifiedTime())) {
            peopleByTime.put(person.getModifiedTime(), new LinkedList<>());
        }
        peopleByTime.get(person.getModifiedTime()).add(person);
    }

    private void removePersonFromMap(PersonWrapper person) {
        List<PersonWrapper> people = peopleByTime.get(person.getModifiedTime());
        people.remove(person);
        if (people.isEmpty()) {
            peopleByTime.remove(person.getModifiedTime());
        }
    }

    @Override
    public boolean pushData(Collection<Person> data, Long instituteId) {
        Timestamp lastSyncTime = lastSync.get(instituteId);
        PersonWrapper personWrapper;
        for (Person person : data) {
            if (person.getModifiedTime().before(lastSyncTime)) {
                lastSync.put(instituteId, person.getModifiedTime());
                instituteService.updateSyncOut(instituteId, person.getModifiedTime());
            }
            personWrapper = person.getPersonWrapper();
            queue.add(personWrapper);
            addPersonToMap(personWrapper);
        }
        return true;
    }

    private PersonWrapper findFirstByMap(Timestamp lastSyncTime) {
        PersonWrapper first = null;
        SortedSet<PersonWrapper> tmpSet, tailSet = new TreeSet<>();
        for (PersonWrapper person : peopleByTime.get(lastSyncTime)) {
            tmpSet = queue.tailSet(person);
            if (tmpSet.size() > tailSet.size()) {
                tailSet = new TreeSet<>(tmpSet);
            }
            queue.tailSet(person).first();
        }
        if (!tailSet.isEmpty()) {
            first = tailSet.first();
        }
        return first;
    }

    private PersonWrapper findFirstByIterator(Timestamp lastSyncTime) {
        PersonWrapper first = null;
        Iterator<PersonWrapper> iterator = queue.iterator();
        while (iterator.hasNext()) {
            first = iterator.next();
            if (first.getModifiedTime() == lastSyncTime) {
                first = iterator.next();
                break;
            }
        }
        return first;
    }

    @Override
    public Collection<PersonWrapper> pullData(Long instituteId, int size) {
        List<PersonWrapper> data = new LinkedList<>();
        Timestamp lastSyncTime = lastSync.get(instituteId);
        PersonWrapper first;
//        first = findFirstByIterator(lastSyncTime);
        first = findFirstByMap(lastSyncTime);
        if (first != null) {
            data.addAll(queue.tailSet(first));
        }
        return data.subList(0, Math.min(size, data.size()));
    }

    private boolean isLastToSync(Long instituteId) {
        return Objects.equals(lastSyncTime(), lastSync.get(instituteId));
    }

    private Timestamp lastSyncTime() {
        return Collections.min(lastSync.values());
    }

    private void reduceQueue() {
        PersonWrapper person;
        Iterator<PersonWrapper> iterator = queue.iterator();
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
        return true;
    }
}

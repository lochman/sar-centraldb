package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.Synchronization;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.SynchronizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class PusherImpl implements Pusher {
    private static final long STEP = 60*60*1000;      // hour
    private static final int BUFFER_SIZE = 100;
    private static final long START_TIME = 0;
    private Synchronization lastSync;
    @Autowired
    PersonRepository personRepository;

    @Autowired
    SynchronizationRepository synchronizationRepository;
    @Override
    public List<Person> fetchData() {
        Collection<Synchronization> syncs = synchronizationRepository.findAll();
        if(!syncs.isEmpty()) {
            Iterator<Synchronization> it = syncs.iterator();
            Synchronization max = it.next();
            while (it.hasNext()) {
                Synchronization next = it.next();
                if (max.getId() < next.getId()) max = next;
            }
            lastSync = max;
        }else{
            lastSync = new Synchronization();
            lastSync.setSyncPointer(new Timestamp(START_TIME));
            synchronizationRepository.save(lastSync);
        }
        List<Person> persons = new ArrayList<>();
        Timestamp startDate = lastSync.getSyncPointer();
        Timestamp endDate = new Timestamp(lastSync.getSyncPointer().getTime()+STEP);
        // TODO - osetrit nedostatek dat
        while (persons.size()<BUFFER_SIZE){
            persons.addAll(getDataByDate(startDate,endDate));
            startDate = endDate;
            endDate = new Timestamp(endDate.getTime()+STEP);
            break; //zakomentuj
        }
        return persons;
    }

    private List<? extends Person> getDataByDate(Timestamp startDate, Timestamp endDate) {
        Optional<Person> o = personRepository.findByDate(startDate, endDate);
        return o.map(Collections::singletonList).orElse(Collections.emptyList());
    }

    public void updateSyncFlags(Timestamp lastTime){
        lastSync.setSyncPointer(lastTime);
        synchronizationRepository.save(lastSync);
    }
}

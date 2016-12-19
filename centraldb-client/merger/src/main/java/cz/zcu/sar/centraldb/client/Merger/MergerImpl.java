package cz.zcu.sar.centraldb.client.Merger;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.Synchronization;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.SynchronizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class MergerImpl implements Merger {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    SynchronizationRepository synchronizationRepository;

    @Override
    public boolean mergeData(List<Person> persons, String batchId){
        personRepository.save(persons);
        Synchronization lastSync;
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
        }
        lastSync.setBatchId(batchId);
        synchronizationRepository.save(lastSync);
        return true;
    }
}

package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.Synchronization;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.SynchronizationRepository;
import cz.zcu.sar.centraldb.client.rest.Sender;
import cz.zcu.sar.centraldb.client.rest.SenderImpl;
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

    private static final long START_TIME = System.currentTimeMillis()-(8*24*60*60*1000); // minus tyden
    private Synchronization lastSync;
    @Autowired
    private BatchCreator creator;
    @Autowired
    Sender sender;
    @Autowired
    SynchronizationRepository synchronizationRepository;

    @Override
    public void pushData() {
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
        if(lastSync.getFistDate()==null){
            lastSync.setLastDate(new Timestamp(START_TIME));
            lastSync.setFistDate(new Timestamp(START_TIME));
            synchronizationRepository.save(lastSync);
        }
        List<Person> persons;
        if(lastSyncComlete()){
            persons = creator.createBatch(lastSync.getLastDate());
            updateSyncFlags();
        }else{
            persons = creator.createBatch(lastSync.getFistDate(),lastSync.getLastDate());
        }
        if(!persons.isEmpty()){
            sender.sendData(persons,lastSync.getBatchId());
        }
    }

    private boolean lastSyncComlete() {
        if (lastSync.getBatchId()==null) return true;
        return sender.sendLastBatchId(lastSync.getBatchId());
    }


    public void updateSyncFlags(){
        lastSync.setLastDate(creator.getEndDate());
        lastSync.setFistDate(creator.getStartDate());
        lastSync.setBatchId(creator.getStartDate().getTime()+";"+creator.getEndDate().getTime());
        synchronizationRepository.save(lastSync);
    }
}

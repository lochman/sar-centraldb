package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.Synchronization;
import cz.zcu.sar.centraldb.client.persistence.services.SynchronizationService;
import cz.zcu.sar.centraldb.client.rest.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class PusherImpl implements Pusher {
    //TODO: into properties
    private static final long START_TIME = System.currentTimeMillis()-(8*24*60*60*1000); // minus tyden
    private Synchronization lastSync;
    @Autowired
    private BatchCreator creator;
    @Autowired
    Sender sender;
    @Autowired
    SynchronizationService synchronizationService;

    @Override
    public void pushData() {
        lastSync = synchronizationService.findLast();
        if (lastSync==null) lastSync = new Synchronization();
        if(lastSync.getFistDate()==null){
            lastSync.setBatchId(null);
            lastSync.setLastDate(new Timestamp(START_TIME));
            lastSync.setFistDate(new Timestamp(START_TIME));
            synchronizationService.save(lastSync);
        }
        List<Person> persons;
        if(lastSyncComlete()){
            persons = creator.createBatch(lastSync.getLastDate());
            updateSyncFlags();
        }else{
            persons = creator.createBatch(lastSync.getFistDate(),lastSync.getLastDate());
        }
        if(!persons.isEmpty()){
            sender.sendData(persons, lastSync.getBatchId());
        }
    }

    private boolean lastSyncComlete() {
        return lastSync.getBatchId()==null ? true :sender.sendLastBatchId(lastSync.getBatchId());
    }


    public void updateSyncFlags(){
        lastSync.setLastDate(creator.getEndDate());
        lastSync.setFistDate(creator.getStartDate());
        lastSync.setBatchId(creator.getStartDate().getTime()+";"+creator.getEndDate().getTime());
        synchronizationService.save(lastSync);
    }
}

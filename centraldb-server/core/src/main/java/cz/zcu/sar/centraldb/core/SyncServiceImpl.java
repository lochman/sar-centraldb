package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.persistence.Person;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import cz.zcu.sar.centraldb.synchronization.SyncQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Matej Lochman on 30.12.16.
 */

@Service
public class SyncServiceImpl implements SyncService {

    @Autowired
    private RequestQueue requestQueue;

    @Autowired
    private SyncQueue syncQueue;

    @Override
    public void pushRequest(Batch batch) {
        //TODO: mark as temp?
        requestQueue.push(batch);
    }

    @Override
    public Batch retrieveData(String instituteId, int size) {
        Batch batch = new Batch();
        try {
            Long id = Long.parseLong(instituteId);
            batch.setPersons(syncQueue.pullData(id, size).toArray(new Person[0]));
            batch.setSize(batch.getPersons().length);
        } catch (NumberFormatException e) {
            //TODO
        }
        return batch;
    }

    @Override
    public boolean confirmBatch(ConfirmFetch confirmed) {
        return false;
    }
}

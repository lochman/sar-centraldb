package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
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

    @Autowired
    private PersonService personService;

    @Autowired
    private InstituteService instituteService;

    @Override
    public Request pushRequest(Batch batch) {
        Request request = requestQueue.push(batch);
        for (Person person : request.getPeople()) {
            personService.savePersonAsTemp(person);
        }
        instituteService.updateBatchId(batch.getClientId(), batch.getId());
        return request;
    }

    @Override
    public Request pullRequest() {
        return requestQueue.pull();
    }

    @Override
    public Batch retrieveData(String instituteId, int size) {
        Batch batch = new Batch();
        try {
            Long id = Long.parseLong(instituteId);
            batch.setPersons(syncQueue.pullData(id, size).toArray(new PersonWrapper[0]));
            batch.setSize(batch.getPersons().length);
        } catch (NumberFormatException e) {
            //TODO log
        }
        return batch;
    }

    @Override
    public boolean confirmBatch(ConfirmFetch confirmed) {
        boolean result;
        try {
            Long id = Long.parseLong(confirmed.getClientId());
            result = syncQueue.updateLastSync(id, confirmed.getLastDate());
        } catch (NumberFormatException e) {
            return false;
        }
        return result;
    }
}

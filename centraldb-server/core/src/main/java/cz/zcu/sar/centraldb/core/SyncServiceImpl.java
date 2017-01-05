package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import cz.zcu.sar.centraldb.merger.Normalizer;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import cz.zcu.sar.centraldb.synchronization.SyncQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Matej Lochman on 30.12.16.
 */

@Service
public class SyncServiceImpl implements SyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Autowired
    private RequestQueue requestQueue;
    @Autowired
    private SyncQueue syncQueue;
    @Autowired
    private PersonService personService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private Normalizer normalizer;

    @Override
    public Request pushRequest(Batch batch) {
        Request request = new Request(batch.getBatchId(),
                batch.getClientId(), normalizer.normalize(batch.getPersons()));
        for (Person person : request.getPeople()) {
            personService.savePersonAsTemp(person.getId());
        }
        instituteService.updateBatchId(batch.getClientId(), batch.getBatchId());
        requestQueue.push(request);
        LOGGER.debug("Pushing request with Id={} into queue", batch.getBatchId());
        return request;
    }

    @Override
    public Request pullRequest() {
        LOGGER.debug("Pulling request from queue");
        return requestQueue.pull();
    }

    @Override
    public Batch retrieveData(String instituteId, int size) {
        Batch batch = new Batch();
        try {
            Long id = Long.parseLong(instituteId);
            batch.setPersons(syncQueue.pullData(id, size).toArray(new PersonWrapper[0]));
            batch.setSize(batch.getPersons().length);
            LOGGER.info("Retrieved data for institute {} of size {}", instituteId, batch.getSize());
        } catch (NumberFormatException e) {
            LOGGER.warn("Failed to parse instituteId {} before pulling from syncQueue", instituteId);
        }
        return batch;
    }

    @Override
    public boolean confirmBatch(ConfirmFetch confirmed) {
        boolean result;
        try {
            Long id = Long.parseLong(confirmed.getClientId());
            result = syncQueue.updateLastSync(id, confirmed.getLastDate());
            LOGGER.info("Confirming batch from institute {} with lastDate {}", confirmed.getClientId(), confirmed.getLastDate());
        } catch (NumberFormatException e) {
            LOGGER.warn("Failed to parse instituteId {} while confirming batch", confirmed.getClientId());
            return false;
        }
        return result;
    }
}

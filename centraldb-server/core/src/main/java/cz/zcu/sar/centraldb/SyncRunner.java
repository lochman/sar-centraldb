package cz.zcu.sar.centraldb;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.core.RequestQueue;
import cz.zcu.sar.centraldb.lookup.LookupPerson;
import cz.zcu.sar.centraldb.merger.Merger;
import cz.zcu.sar.centraldb.merger.Normalizer;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 03.01.2017.
 */
@Service
public class SyncRunner extends Thread {
    long timeout = 1000;
    @Autowired
    TestDataLoader testDataLoader;
    @Autowired
    Normalizer normalizer;
    @Autowired
    Merger merger;
    @Autowired
    PersonService personService;
    @Autowired
    RequestQueue requestQueue;
    @Autowired
    LookupPerson lookupPerson;
    @Autowired
    InstituteService instituteService;

    @Override
    public synchronized void run() {
        testDataLoader.run();
        while (true) {
            if (!requestQueue.empty()) {
                Batch batch = requestQueue.pull();
                Optional<Institute> optional = instituteService.findOne(Long.parseLong(batch.getClientId()));
                if (optional.isPresent()) {
                    PersonWrapper persons[] = batch.getPersons();
                    for (int i = 0; i < persons.length; i++) {
                        Person person = normalizer.normalize(persons[i]);
                        Person persist;
                        person = personService.savePersonAsTemp(person);
                        if (person.getId() == null) {
                            persist = lookupPerson.findPerson(person);
                        } else {
                            persist = personService.findPerson(person.getId());
                        }
                        merger.mergeData(person, persist);
                    }
                    Institute institute = optional.get();
                    instituteService.updateBatchId(institute, batch.getClientId());
                }
            }
            try {
                wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package cz.zcu.sar.centraldb;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.core.Request;
import cz.zcu.sar.centraldb.core.RequestQueue;
import cz.zcu.sar.centraldb.core.TestDataLoader;
import cz.zcu.sar.centraldb.lookup.PersonLookup;
import cz.zcu.sar.centraldb.merger.Merger;
import cz.zcu.sar.centraldb.merger.Normalizer;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
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
    PersonLookup lookupPerson;
    @Autowired
    InstituteService instituteService;


    @PostConstruct
    private void initData(){
        testDataLoader.run(false,100);
        testDataLoader.run(true,200);
        Request request = new Request();
        request.setPeople(personService.initMergeBuffer());
        requestQueue.push(request);
    }

    @Override
    public void run() {
        while (true) {
            Request request = requestQueue.pull();
            if (request==null) {
                sleep();
                continue;
            }
            List<Person> persons = request.getPeople();
            if (persons==null) {
                sleep();
                continue;
            }
            for (Person temp : persons) {
//                Person person = normalizer.normalize(person1);
                Person persist;
                if (temp.getForeignId() == null) {
                    persist = lookupPerson.findPerson(temp);
                } else {
                    persist = personService.findPerson(temp.getForeignId());
                }
                merger.mergeData(temp, persist);
            }
        }
    }
    private synchronized void sleep(){
        try {
            wait(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public synchronized void run() {
//        testDataLoader.run();
//        while (true) {
//            if (!requestQueue.empty()) {
//                Batch batch = requestQueue.pull();
//                Optional<Institute> optional = instituteService.findOne(Long.parseLong(batch.getClientId()));
//                if (optional.isPresent()) {
//                    PersonWrapper persons[] = batch.getPersons();
//                    for (PersonWrapper person1 : persons) {
//                        Person person = normalizer.normalize(person1);
//                        Person persist;
//                        person = personService.savePersonAsTemp(person);
//                        if (person.getId() == null) {
//                            persist = lookupPerson.findPerson(person);
//                        } else {
//                            persist = personService.findPerson(person.getId());
//                        }
//                        merger.mergeData(person, persist);
//                    }
//                    Institute institute = optional.get();
//                    instituteService.updateBatchId(institute, batch.getClientId());
//                }
//            }
//            try {
//                wait(timeout);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                break;
//            }
//        }
//    }
}

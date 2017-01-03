package cz.zcu.sar.centraldb;

import cz.zcu.sar.centraldb.core.Request;
import cz.zcu.sar.centraldb.core.RequestQueue;
import cz.zcu.sar.centraldb.core.TestDataLoader;
import cz.zcu.sar.centraldb.lookup.PersonLookup;
import cz.zcu.sar.centraldb.merger.Merger;
import cz.zcu.sar.centraldb.merger.Normalizer;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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
            if (!requestQueue.isEmpty()) {
                Request request = requestQueue.pull();
                List<Person> persons = request.getPeople();
                for (Person temp : persons) {
                    Person persist;
                    if (temp.getForeignId() == null) {
                        persist = lookupPerson.findPerson(temp);
                    } else {
                        persist = personService.findPerson(temp.getForeignId());
                    }
                    merger.mergeData(temp, persist);
                }
            }else{
                sleep();
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
}

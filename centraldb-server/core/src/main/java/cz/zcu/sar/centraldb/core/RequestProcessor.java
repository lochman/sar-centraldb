package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.lookup.PersonLookup;
import cz.zcu.sar.centraldb.merger.Merger;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Matej Lochman on 2.1.17.
 */

@Component
public class RequestProcessor implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);

    @Autowired
    private RequestQueue requestQueue;
    @Autowired
    private Merger merger;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonLookup personLookup;

    private final long TIMEOUT = 1000;

    private void processRequest() {
        Request request = requestQueue.pull();
        for (Person person : request.getPeople()) {
            Person persist;
            if (person.getId() == null) {
                persist = personLookup.findPerson(person);
            } else {
                persist = personService.findPerson(person.getId());
            }
            merger.mergeData(person, persist);
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("Request processor started");
        while (true) {
            if (!requestQueue.isEmpty()) {
                processRequest();
            }
            try {
                wait(TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

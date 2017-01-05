package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.lookup.PersonLookup;
import cz.zcu.sar.centraldb.merger.Merger;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Marek Rasocha
 *         date 03.01.2017.
 */
@Component
public class RequestProcessor implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);
    @Autowired
    private TestDataLoader testDataLoader;
    @Autowired
    private Merger merger;
    @Autowired
    private PersonService personService;
    @Autowired
    private RequestQueue requestQueue;
    @Autowired
    private PersonLookup personLookup;

    @Value("${requestProcessor.timeout}")
    private long TIMEOUT;

    @PostConstruct
    private void initData() {
        testDataLoader.run(false, 100);
        testDataLoader.run(true, 200);
        Request request = new Request();
        request.setPeople(personService.initMergeBuffer());
        if (!request.getPeople().isEmpty()) {
            requestQueue.push(request);
        }
        LOGGER.info("Request processor started");
    }

    private void processRequest() {
        Request request = requestQueue.pull();
        for (Person person : request.getPeople()) {
            Person persist;
            if (person.getForeignId() == null) {
                persist = personLookup.findPerson(person);
            } else {
                persist = personService.findPerson(person.getForeignId());
            }
            Person mergerPerson = merger.mergeData(person, persist);
        }
        LOGGER.info("Processed request {} from client {}.", request.getBatchId(), request.getClientId());
    }

    @Override
    public void run(String... strings) throws Exception {
        while (true) {
            if (!requestQueue.isEmpty()) {
                processRequest();
            } else {
                sleep();
            }
        }
    }

    private synchronized void sleep() {
        try {
            wait(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

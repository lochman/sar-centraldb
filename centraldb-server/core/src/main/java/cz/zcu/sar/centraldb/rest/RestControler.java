package cz.zcu.sar.centraldb.rest;

import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import cz.zcu.sar.centraldb.core.Request;
import cz.zcu.sar.centraldb.core.RequestQueue;
import cz.zcu.sar.centraldb.core.SyncService;
import cz.zcu.sar.centraldb.core.SyncServiceImpl;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import cz.zcu.sar.centraldb.synchronization.SyncQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
@RestController
public class RestControler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestControler.class);
    @Autowired
    private SyncService syncService;

    @Autowired
    private SyncQueue syncQueue;

    @Autowired
    private RequestQueue requestQueue;

    @Autowired
    private InstituteService instituteService;

    @PostMapping("/lastBatch")
    public ResponseEntity<String> getLastBatch(@RequestBody String instituteId) {
        Optional<Institute> institute;
        try {
            Long id = Long.parseLong(instituteId);
            institute = instituteService.findOne(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (institute.isPresent() && institute.get().getLastBatchId() != null) {
            String id = institute.get().getLastBatchId();
            LOGGER.info("Last batch id of institute {} is {}", instituteId, id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            LOGGER.info("Institute {} has no lastBatchId yet", instituteId);
            return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
        }
    }
    /*this is only sync method that expects person to be persisted by web module*/
    @PostMapping("web/person")
    public ResponseEntity<String> createPerson(@RequestBody() Person person) {
        List<Person> list = new ArrayList();
        list.add(person);
        requestQueue.push(new Request(null, null, list));
        LOGGER.info("createPerson from web called " + person);
        return new ResponseEntity<>("{\"status\": true}", HttpStatus.OK);
    }
    /*this is only sync method that expects person to be persisted by web module*/
    @PutMapping("web/person/{id}")
    public ResponseEntity<String> updatePerson(@PathVariable Long id, @RequestBody() Person person) {
        if (person == null || id != person.getId()){
            return new ResponseEntity<>("{\"status\": false}", HttpStatus.NOT_FOUND);
        }
        List<Person> list = new ArrayList();
        list.add(person);
        syncQueue.pushData(list, null);
        LOGGER.info("updatePerson from web called " + person);
        return new ResponseEntity<>("{\"status\": true}", HttpStatus.OK);
    }


    @PostMapping("/data")
    public ResponseEntity<Batch> getData(@RequestBody() Batch batch) {
        syncService.pushRequest(batch);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/data/fetch")
    public ResponseEntity<Batch> fetchData(@RequestBody() Batch batchRequest) {
        Batch batch = syncService.retrieveData(batchRequest.getClientId(), batchRequest.getSize());
        return new ResponseEntity<>(batch, HttpStatus.OK);
    }

    @PostMapping("/data/confirm")
    public ResponseEntity<ConfirmFetch> confirmFetch(@RequestBody() ConfirmFetch batch) {
        HttpStatus status = syncService.confirmBatch(batch) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(status);
    }
}

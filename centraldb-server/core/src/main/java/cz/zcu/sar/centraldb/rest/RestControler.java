package cz.zcu.sar.centraldb.rest;

import cz.zcu.sar.centraldb.common.persistence.Person;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import cz.zcu.sar.centraldb.core.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
@RestController
public class RestControler {

    @Autowired
    private SyncService syncService;

    @PostMapping("/lastBatch")
    public ResponseEntity<String> lastBatch(@RequestBody String instituteId) {
        //todo: najdi lastBatch podle id clienta
        String result = "laswtBactch";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/data")
    public ResponseEntity<Batch> getData(@RequestBody() Batch batch) {
        //todo: vloz do bufferu
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/data/fetch")
    public ResponseEntity<Batch> fetchData(@RequestBody() ConfirmFetch confirmed) {
        // TODO: nacti data z fronty a posli je a normalizuj
        Person[] persons = new Person[1];
        persons[0] = new Person();
        persons[0].setName("asd");
        persons[0].setModifiedBy("user1");
        persons[0].setModifiedTime(new Timestamp(System.currentTimeMillis()));
        persons[0].setBirthDate(new Timestamp(System.currentTimeMillis()));
        Batch batch = syncService.retrieveData(confirmed.getClientId(), confirmed.getSize());
        batch.setPersons(persons);
        return new ResponseEntity<>(batch, HttpStatus.OK);
    }

    @PostMapping("/data/confirm")
    public ResponseEntity<ConfirmFetch> confirmFetch(@RequestBody() ConfirmFetch batch) {
        //todo: potvrd prijeti davky
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

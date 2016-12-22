package cz.zcu.sar.centraldb.rest;


import cz.zcu.sar.centraldb.common.persistence.Person;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;


/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
@RestController
public class RestControler {



    @RequestMapping(value = "/lastBatch", method = RequestMethod.POST)
    public ResponseEntity<String> lastBatch(@RequestBody String id) {
        //todo: najdi lastBatch podle id clienta
        String result = "laswtBactch";
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public ResponseEntity<Batch> getData(@RequestBody() Batch batch) {
        //todo: vloz do bufferu
        return new ResponseEntity<Batch>(HttpStatus.OK);
    }
    @RequestMapping(value = "/data/fetch", method = RequestMethod.POST)
    public ResponseEntity<Batch> fetchData(@RequestBody() Batch batch) {
        // TODO: nacti data z fronty a posli je a normalizuj
        Person[] persons = new Person[1];
        persons[0] = new Person();
        persons[0].setName("asd");
        persons[0].setModifiedBy("user1");
        persons[0].setModifiedTime(new Timestamp(System.currentTimeMillis()));
        persons[0].setBirthDate(new Timestamp(System.currentTimeMillis()));
        batch.setPersons(persons);
        return new ResponseEntity<Batch>(batch,HttpStatus.OK);
    }
    @RequestMapping(value = "/data/confirm", method = RequestMethod.POST)
    public ResponseEntity<ConfirmFetch> confirmFetch(@RequestBody() ConfirmFetch batch) {
        //todo: potvrd prijeti davky
        return new ResponseEntity<ConfirmFetch>(HttpStatus.OK);
    }

}

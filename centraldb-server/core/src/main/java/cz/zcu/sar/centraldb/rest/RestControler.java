package cz.zcu.sar.centraldb.rest;


import cz.zcu.sar.centraldb.common.persistence.Person;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Batch> getData(@RequestBody() Batch bacth) {
        //todo: vloz do bufferu
        return new ResponseEntity<Batch>(HttpStatus.OK);
    }

}

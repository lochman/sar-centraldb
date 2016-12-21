package cz.zcu.sar.centraldb.rest;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

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
    public ResponseEntity<String> getData(@RequestBody String id) {
//        params.put("idClient", clientId);
        //todo: najdi lastBatch podle id clienta
        JSONObject result = new JSONObject();
        return new ResponseEntity<String>("lastBatch", HttpStatus.OK);
    }

}

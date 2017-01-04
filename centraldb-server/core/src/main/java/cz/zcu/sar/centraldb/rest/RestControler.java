package cz.zcu.sar.centraldb.rest;

import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;
import cz.zcu.sar.centraldb.core.SyncService;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.service.InstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */
@RestController
public class RestControler {

    @Autowired
    private SyncService syncService;

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
        return institute.isPresent() ? new ResponseEntity<>(institute.get().getLastBatchId(), HttpStatus.OK) : new ResponseEntity<>("NOT FOUND",HttpStatus.NOT_FOUND);
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

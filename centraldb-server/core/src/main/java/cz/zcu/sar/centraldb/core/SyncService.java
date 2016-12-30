package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.common.synchronization.ConfirmFetch;

/**
 * Created by Matej Lochman on 30.12.16.
 */

public interface SyncService {
    void pushToQueue(Batch batch);
    Batch retrieveData(String instituteId, int size);
    boolean confirmBatch(ConfirmFetch confirmed);
}

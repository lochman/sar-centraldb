package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.synchronization.Batch;

/**
 * Created by Matej Lochman on 30.12.16.
 */

public interface RequestQueue {
    Request push(Request request);
    Request pull();
    Request peek();
    boolean isPresent(Batch batch);
    int size();
    boolean isEmpty();
}

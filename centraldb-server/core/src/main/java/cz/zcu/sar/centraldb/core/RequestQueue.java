package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.synchronization.Batch;

/**
 * Created by Matej Lochman on 30.12.16.
 */

public interface RequestQueue {
    void push(Batch batch);
    Batch pull();
    Batch peek();
    int size();
    boolean empty();
}

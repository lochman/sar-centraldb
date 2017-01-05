package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.merger.Normalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Matej Lochman on 30.12.16.
 */

@Component
public class RequestQueueImpl implements RequestQueue {

    private Queue<Request> queue;

    RequestQueueImpl() {

    }

    @PostConstruct
    private void init() {
        queue = new LinkedList<>();
    }

    @Override
    public Request push(Request request){
        queue.add(request);
        return request;
    }

    @Override
    public Request pull() {
        return queue.poll();
    }

    @Override
    public Request peek() {
        return queue.peek();
    }

    @Override
    public boolean isPresent(Batch batch) {
        return queue.contains(new Request(batch.getBatchId(), batch.getClientId()));
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

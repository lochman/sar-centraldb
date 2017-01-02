package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.synchronization.Batch;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Matej Lochman on 30.12.16.
 */

@Component
public class RequestQueueImpl implements RequestQueue {

    Queue<Batch> queue;

    RequestQueueImpl() {
//        queue = new PriorityQueue<>((Batch b1, Batch b2) -> b1.getFirst().compareTo(b2.getFirst()));
        queue = new LinkedList<>();
    }

    @Override
    public void push(Batch batch) {
        queue.add(batch);
    }

    @Override
    public Batch pull() {
        return queue.poll();
    }

    @Override
    public Batch peek() {
        return queue.peek();
    }

    @Override
    public boolean isPresent(Batch batch) {
        return queue.contains(batch);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean empty() {
        return queue.isEmpty();
    }
}

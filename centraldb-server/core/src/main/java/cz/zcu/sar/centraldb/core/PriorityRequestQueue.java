package cz.zcu.sar.centraldb.core;

import cz.zcu.sar.centraldb.common.synchronization.Batch;
import org.springframework.stereotype.Component;

import java.util.PriorityQueue;

/**
 * Created by Matej Lochman on 30.12.16.
 */

@Component
public class PriorityRequestQueue implements RequestQueue {

    PriorityQueue<Batch> queue;

    PriorityRequestQueue() {
        queue = new PriorityQueue<>((Batch b1, Batch b2) -> b1.getFirst().compareTo(b2.getFirst()));
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
    public int size() {
        return queue.size();
    }

    @Override
    public boolean empty() {
        return queue.isEmpty();
    }
}

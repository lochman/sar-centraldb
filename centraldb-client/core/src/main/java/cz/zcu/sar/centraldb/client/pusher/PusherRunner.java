package cz.zcu.sar.centraldb.client.pusher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */

@Component
@Scope("prototype")
public class PusherRunner extends Thread {
    @Value("${thread.pusher.timeout}")
    private long timeout;
    @Autowired
    private Pusher pusher;

    @Autowired
    TestDataLoader testDataLoader;

    public synchronized void run(){
        boolean running = true;
        while (running) {
            pusher.pushData();
            try {
                wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

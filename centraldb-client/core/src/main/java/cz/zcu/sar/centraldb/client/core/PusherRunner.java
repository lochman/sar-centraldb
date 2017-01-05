package cz.zcu.sar.centraldb.client.core;

import cz.zcu.sar.centraldb.client.pusher.Pusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Thread for pusher
 * @author Marek Rasocha
 *         date 16.12.2016.
 */

@Component
@Scope("prototype")
public class PusherRunner extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherRunner.class);
    @Value("${thread.pusher.timeout}")
    private long timeout;
    @Autowired
    private Pusher pusher;

    /**
     * push data to server
     */
    public synchronized void run(){
        LOGGER.info("Pusher started with timeout set to {}", timeout);
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

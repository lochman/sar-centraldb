package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.fetcher.Fetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Thread for fetcher
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Component
public class FetcherRunner extends Thread{
    private static final Logger LOGGER = LoggerFactory.getLogger(FetcherRunner.class);
    @Value("${thread.fetcher.timeout}")
    private long timeout;
    @Autowired
    private Fetcher fetcher;

    /**
     * Fetch data from server
     */
    public synchronized void run() {
        LOGGER.info("Fetcher started with timeout set to {}", timeout);
        boolean running=true;
        while (running){
            fetcher.fetchData();
            try {
                wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

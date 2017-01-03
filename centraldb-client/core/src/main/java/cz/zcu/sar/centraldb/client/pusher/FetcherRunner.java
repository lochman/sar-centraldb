package cz.zcu.sar.centraldb.client.pusher;

import cz.zcu.sar.centraldb.client.fetcher.Fetcher;
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

    @Value("${thread.fetcher.timeout}")
    private long timeout;
    @Autowired
    private Fetcher fetcher;

    @Autowired
    TestDataLoader testDataLoader;

    /**
     * Fetch data from server
     */
    public synchronized void run(){
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

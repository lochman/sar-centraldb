package cz.zcu.sar.centraldb.synchronization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Matej Lochman on 23.12.16.
 */

@Component
public class Synchronization implements CommandLineRunner {

    @Autowired
    private SyncQueue syncQueue;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Synchronization module starting.");
        syncQueue.pullData(0L, 0);
    }
}

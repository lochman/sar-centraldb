package cz.zcu.sar.centraldb.client.pusher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Marek Rasocha
 *         date 30.12.2016.
 */
@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    FetcherRunner fetcherRunner;
    @Autowired
    PusherRunner pusherRunner;

    @Override
    public void run(String... args) throws Exception {
        pusherRunner.start();
//        fetcherRunner.start();
        pusherRunner.join();
//        fetcherRunner.join();

    }
}

package cz.zcu.sar.centraldb.client.core;

import cz.zcu.sar.centraldb.client.core.utils.TestDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Client starter
 * @author Marek Rasocha
 *         date 30.12.2016.
 */
@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    FetcherRunner fetcherRunner;
    @Autowired
    PusherRunner pusherRunner;
    @Autowired
    TestDataLoader testDataLoader;
    @Value("${testDataCount}")
    private int testDataCount;

    @Override
    public void run(String... args) throws Exception {
        testDataLoader.generatePeople(testDataCount);
        pusherRunner.start();
        fetcherRunner.start();
        pusherRunner.join();
        fetcherRunner.join();
    }
}

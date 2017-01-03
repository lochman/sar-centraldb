package cz.zcu.sar.centraldb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Matej Lochman on 16.12.2016.
 */

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    SyncRunner syncRunner;


    public void run(String... strings) throws Exception {
       syncRunner.start();
    }


}

package cz.zcu.sar.centraldb.client.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Matej Lochman on 16.12.2016.
 */

@SpringBootApplication(scanBasePackages = {"cz.zcu.sar.centraldb.client"})
@EntityScan("cz.zcu.sar.centraldb.client.persistence.domain")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

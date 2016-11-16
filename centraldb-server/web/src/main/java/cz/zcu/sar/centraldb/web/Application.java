package cz.zcu.sar.centraldb.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@SpringBootApplication(scanBasePackages = {"cz.zcu.sar.centraldb.web"})
@EnableJpaRepositories("cz.zcu.sar.centraldb.persistence.repository")
@EntityScan("cz.zcu.sar.centraldb.persistence.domain")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

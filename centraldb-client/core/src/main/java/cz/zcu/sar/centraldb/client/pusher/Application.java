package cz.zcu.sar.centraldb.client.pusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Matej Lochman on 16.12.2016.
 */

@SpringBootApplication(scanBasePackages = {"cz.zcu.sar.centraldb.client"})
@ComponentScan("cz.zcu.sar.centraldb.client")
@EnableJpaRepositories(basePackages="cz.zcu.sar.centraldb.client.persistence.repository")
@EntityScan("cz.zcu.sar.centraldb.client")
public class Application{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

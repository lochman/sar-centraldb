package cz.zcu.sar.centraldb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Marek Rasocha
 *         date 21.12.2016.
 */


@SpringBootApplication(scanBasePackages = {"cz.zcu.sar.centraldb"})
@EnableJpaRepositories("cz.zcu.sar.centraldb.persistence.repository")
@EntityScan("cz.zcu.sar.centraldb.persistence.domain")
@ComponentScan("cz.zcu.sar.centraldb")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

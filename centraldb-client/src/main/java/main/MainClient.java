package main;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Marek Rasocha
 *         date 13.12.2016.
 */

/*@SpringBootApplication(scanBasePackages = {"cz.zcu.sar.centraldb.client"})
@EnableJpaRepositories("cz.zcu.sar.centraldb.client.persistence.repository")
@EntityScan("cz.zcu.sar.centraldb.client.persistence.domain")*/
public class MainClient {

    public static void main(String[] args) {
        //SpringApplication.run(MainClient.class, args);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("main/spring.xml");

        HelloWord obj = (HelloWord) context.getBean("helloWorld");

        obj.getMessage();
    }

}

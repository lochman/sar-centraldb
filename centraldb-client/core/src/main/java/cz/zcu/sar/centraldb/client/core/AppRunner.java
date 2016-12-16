package cz.zcu.sar.centraldb.client.core;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Matej Lochman on 16.12.2016.
 */

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    public void run(String... strings) throws Exception {
        Person person = new Person();
        person.setName("Karel");
        personRepository.save(person);
        System.out.println(personRepository.findAll().toString());
    }
}

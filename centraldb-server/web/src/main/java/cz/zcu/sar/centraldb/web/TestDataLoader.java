package cz.zcu.sar.centraldb.web;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Matej Lochman on 22.12.16.
 */

@Component
public class TestDataLoader implements ApplicationRunner {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Person person;
        for (int i = 0; i < 100; i++) {
            person = new Person("Jmeno" + i % 15, "Prijmeni" + i, "");
            if (i % 2 == 0) { person.setGender("m");
            } else { person.setGender("f"); }
            person.setModifiedBy("init");
            person.setBirthDate(new Date());
            personRepository.save(person);
        }
    }
}

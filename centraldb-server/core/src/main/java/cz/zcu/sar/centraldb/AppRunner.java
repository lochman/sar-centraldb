package cz.zcu.sar.centraldb;

import cz.zcu.sar.centraldb.lookup.LookupPerson;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.PersonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Matej Lochman on 16.12.2016.
 */

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    PersonTypeService personTypeService;
    @Autowired
    TestDataLoader testDataLoader;
    @Autowired
    LookupPerson lookupPerson;
    public void run(String... strings) throws Exception {
        testDataLoader.run();
        testDataLoader.run_temp();
        Person person = new Person();
        person.setSocialNumber("1");
        person.setCompanyNumber("1");
        Person p = lookupPerson.findPerson(person);

        person.setSocialNumber("2");
        person.setCompanyNumber(null);
        Person p2 = lookupPerson.findPerson(person);

        person.setCompanyNumber("3");
        Person p3 = lookupPerson.findPerson(person);

        System.out.println();
    }
}

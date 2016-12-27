package cz.zcu.sar.centraldb.web;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.persistence.repository.AddressTypeRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonTypeRepository;
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

    @Autowired
    private AddressRepository addresssRepository;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private AddressTypeRepository addressTypeRepository;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Person person;
        //Create person types
        PersonType t1 = new PersonType();
        t1.setDescription("Fyzická osoba");
        t1.setModifiedBy("init");
        personTypeRepository.save(t1);
        PersonType t2 = new PersonType();
        t2.setDescription("Právnická osoba");
        t2.setModifiedBy("init");
        personTypeRepository.save(t2);

        //Create address types
        AddressType at1 = new AddressType();
        at1.setDescription("Trvalá adresa");
        at1.setModifiedBy("init");
        addressTypeRepository.save(at1);
        AddressType at2 = new AddressType();
        at2.setDescription("Přechodná adresa");
        at2.setModifiedBy("init");
        addressTypeRepository.save(at2);

        for (int i = 0; i < 112; i++) {
            Address address = new Address();
            person = new Person("Jmeno" + i % 15, "Prijmeni" + i, "");
            if (i % 2 == 0) { person.setGender("m");
            } else { person.setGender("f"); }
            person.setModifiedBy("init");
            if( i % 5 == 0) {
                person.setPersonType(t2);
                address.setAddressType(at2);
            }
            else{
                person.setPersonType(t1);
                address.setAddressType(at1);
            }
            person.setBirthDate(new Date());
            personRepository.save(person);
            address.setStreet("Dlouhá");
            address.setLand_registry_number(String.valueOf(i));
            address.setCity("Praha");
            address.setModifiedBy("init");
            address.setPerson(person);
            addresssRepository.save(address);

        }
    }
}

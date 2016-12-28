package cz.zcu.sar.centraldb.client.pusher;


import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressTypeRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonTypeRepository;
import cz.zcu.sar.centraldb.client.persistence.services.BaseService;

import cz.zcu.sar.centraldb.client.persistence.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Matej Lochman on 22.12.16.
 */

@Service
public class TestDataLoader{

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addresssRepository;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private AddressTypeRepository addressTypeRepository;

    @Autowired
    private BaseService baseService;

    @Autowired
    private PersonService personService;

    public void run(){
        //Create person types
        Random random = new Random();
        List<PersonType> personTypes = personTypeRepository.findAll();
        if (personTypes.isEmpty()){
            personTypes.addAll(initPersonType());
        }
        List<AddressType> addressTypes = addressTypeRepository.findAll();
        if (addressTypes.isEmpty()){
            addressTypes.addAll(initAddressType());
        }
        for (int i = 0; i < 100; i++) {
            Address address = new Address();
            Address address2 = new Address();
            Person person = new Person("Jmeno" + i, "Prijmeni" , "");
            if (i % 2 == 0) { person.setGender("m");
            } else { person.setGender("f"); }
            person.setModifiedBy("user1");
            person.setCompanyNumber(String.valueOf(i));
            person.setSocialNumber(String.valueOf(i));
            // generate modified time
            long curr = System.currentTimeMillis();
            long hour = 60*60*1000;
            int cislo = random.nextInt(7*24);        // 7 dni a 24 hod
            person.setModifiedTime(new Timestamp(curr-(hour*cislo)));

            if( i % 5 == 0) {
                person.setPersonType(personTypes.get(0));
                address.setAddressType(addressTypes.get(0));
                address2.setAddressType(addressTypes.get(0));
            }
            else{
                person.setPersonType(personTypes.get(1));
                address.setAddressType(addressTypes.get(1));
                address2.setAddressType(addressTypes.get(1));
            }
            person.setBirthDate(new Date());
           // personRepository.save(person);
            if(i%10 !=0){
                address.setStreet("Dlouhá" + i);
                address.setLand_registry_number(String.valueOf(i));
                address.setCity("Praha" + i);
                address = (Address) baseService.setDefaultValue(address,false);
                address.setPerson(person);

                address2.setStreet("Kratka" + i);
                address2.setLand_registry_number(String.valueOf(i));
                address2.setCity("Plisen" + i);
                address2 = (Address) baseService.setDefaultValue(address2,false);
                address2.setPerson(person);
                Set set = new HashSet<>();
                set.add(address);
                set.add(address2);
                person.setAddressWrappers(set);
//                addresssRepository.save(address);
//                addresssRepository.save(address2);
            }
            personService.createPerson(person);





        }
    }

    private List<AddressType> initAddressType() {
        //Create address types
        AddressType at1 = new AddressType();
        at1.setDescription("Trvalá adresa");
        at1 = (AddressType) baseService.setDefaultValue(at1,false);
        addressTypeRepository.save(at1);
        AddressType at2 = new AddressType();
        at2.setDescription("Přechodná adresa");
        at2 = (AddressType) baseService.setDefaultValue(at2,false);
        addressTypeRepository.save(at2);
        return addressTypeRepository.findAll();
    }

    private List<PersonType> initPersonType() {
        PersonType t1 = new PersonType();
        t1.setDescription("Fyzická osoba");
        t1 = (PersonType) baseService.setDefaultValue(t1,false);
        personTypeRepository.save(t1);
        PersonType t2 = new PersonType();
        t2.setDescription("Právnická osoba");
        t2 = (PersonType) baseService.setDefaultValue(t2,false);
        personTypeRepository.save(t2);
        return personTypeRepository.findAll();

    }
}
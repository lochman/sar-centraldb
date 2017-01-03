package cz.zcu.sar.centraldb.core;


import cz.zcu.sar.centraldb.common.persistence.domain.AddressTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.merger.UtilService;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.service.AddressTypeService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import cz.zcu.sar.centraldb.persistence.service.PersonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Matej Lochman on 22.12.16.
 */

@Service
public class TestDataLoader {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonTypeService personTypeService;

    @Autowired
    private AddressTypeService addressTypeService;

    @Autowired
    private UtilService utilService;






    public void initTypes(){
        List<PersonType> personTypes = personTypeService.findAll();
        if (personTypes.isEmpty()){
            personTypes.addAll(initPersonType());
        }
        List<AddressType> addressTypes = addressTypeService.findAll();
        if (addressTypes.isEmpty()){
            addressTypes.addAll(initAddressType());
        }
    }
    public void run(boolean temp,int number){
        //Create person types
        Random random = new Random();
        List<PersonType> personTypes = personTypeService.findAll();
        if (personTypes.isEmpty()){
            personTypes.addAll(initPersonType());
        }
        List<AddressType> addressTypes = addressTypeService.findAll();
        if (addressTypes.isEmpty()){
            addressTypes.addAll(initAddressType());
        }
        for (int i = 0; i < number; i++) {
            Address address = new Address();
            Address address2 = new Address();
            Person person = new Person("Jmeno" + i, "Prijmeni" , "");
            person.setTemporary(temp);
            if (i % 2 == 0) { person.setGender("m");
            } else { person.setGender("f"); }
            person.setModifiedBy("init");
            person = (Person) utilService.setDefaultValue(person,false);
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
                address = (Address) utilService.setDefaultValue(address,false);
                address.setPerson(person);

                address2.setStreet("Kratka" + i);
                address2.setLand_registry_number(String.valueOf(i));
                address2.setCity("Plisen" + i);
                address2 = (Address) utilService.setDefaultValue(address2,false);
                address2.setPerson(person);
                Set<Address> set = new HashSet<>();
                set.add(address);
                set.add(address2);
                person.setAddressWrappers(set);
            }
            personService.createPerson(person);
        }
    }

    public PersonWrapper[] run_temp(int number){
        PersonWrapper[] personWrappers = new PersonWrapper[number];
        //Create person types
        //List<PersonWrapper> personWrappers = new ArrayList<>();
        Random random = new Random();
        List<PersonTypeWrapper> personTypes = (initPersonTypeW());

        List<AddressTypeWrapper> addressTypes = initAddressTypeW();
        for (int i = 0; i < number; i++) {
            AddressWrapper<PersonWrapper,AddressTypeWrapper> address = new AddressWrapper<>();
            AddressWrapper<PersonWrapper,AddressTypeWrapper> address2 = new AddressWrapper<>();
            PersonWrapper<PersonTypeWrapper,AddressWrapper> person = new PersonWrapper<>("Jmeno" + i, "Prijmeni" , "");
            if (i % 2 == 0) { person.setGender("m");
            } else { person.setGender("f"); }
            person = (PersonWrapper<PersonTypeWrapper, AddressWrapper>) utilService.setDefaultValue(person,false);
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
                address = (AddressWrapper<PersonWrapper, AddressTypeWrapper>) utilService.setDefaultValue(address,false);
                address.setPerson(person);

                address2.setStreet("Kratka" + i);
                address2.setLand_registry_number(String.valueOf(i));
                address2.setCity("Plisen" + i);
                address2 = (AddressWrapper<PersonWrapper, AddressTypeWrapper>) utilService.setDefaultValue(address2,false);
                address2.setPerson(person);
                Set<AddressWrapper> set = new HashSet<>();
                set.add(address);
                set.add(address2);
                person.setAddressWrappers(set);
            }
            personWrappers[i]=person;
//            personService.savePersonAsTemp(person);
        }
        return personWrappers;
    }


    private List<AddressTypeWrapper> initAddressTypeW() {
        //Create address types
        List<AddressType> addressTypes = addressTypeService.findAll();
        AddressTypeWrapper at1 = new AddressTypeWrapper();
        at1.setId(addressTypes.get(0).getId());
        at1.setDescription("Trvalá adresa");
        at1 = (AddressTypeWrapper) utilService.setDefaultValue(at1,false);
        //addressTypeService.save(at1);
        AddressTypeWrapper at2 = new AddressTypeWrapper();
        at2.setId(addressTypes.get(1).getId());
        at2.setDescription("Přechodná adresa");
        at2 = (AddressTypeWrapper) utilService.setDefaultValue(at2,false);
//        addressTypeService.save(at2);
//        return addressTypeService.findAll();
        List<AddressTypeWrapper> list = new ArrayList<>();
        list.add(at1);
        list.add(at2);
        return list;
    }

    private List<PersonTypeWrapper> initPersonTypeW(){
        List<PersonType> personTypes = personTypeService.findAll();
        PersonTypeWrapper t1 = new PersonTypeWrapper();
        t1.setId(personTypes.get(0).getId());
        t1.setDescription("Fyzická osoba");
        t1 = (PersonTypeWrapper) utilService.setDefaultValue(t1,false);
//        personTypeService.save(t1);
        PersonTypeWrapper t2 = new PersonTypeWrapper();
        t2.setId(personTypes.get(1).getId());
        t2.setDescription("Právnická osoba");
        t2 = (PersonTypeWrapper) utilService.setDefaultValue(t2,false);
//        personTypeService.save(t2);
        List<PersonTypeWrapper> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        return list;
    }

    private List<AddressType> initAddressType() {
        //Create address types
        AddressType at1 = new AddressType();
        at1.setDescription("Trvalá adresa");
        at1 = (AddressType) utilService.setDefaultValue(at1,false);
        addressTypeService.save(at1);
        AddressType at2 = new AddressType();
        at2.setDescription("Přechodná adresa");
        at2 = (AddressType) utilService.setDefaultValue(at2,false);
        addressTypeService.save(at2);
        return addressTypeService.findAll();
    }

    private List<PersonType> initPersonType() {
        PersonType t1 = new PersonType();
        t1.setDescription("Fyzická osoba");
        t1 = (PersonType) utilService.setDefaultValue(t1,false);
        personTypeService.save(t1);
        PersonType t2 = new PersonType();
        t2.setDescription("Právnická osoba");
        t2 = (PersonType) utilService.setDefaultValue(t2,false);
        personTypeService.save(t2);
        return personTypeService.findAll();

    }
}
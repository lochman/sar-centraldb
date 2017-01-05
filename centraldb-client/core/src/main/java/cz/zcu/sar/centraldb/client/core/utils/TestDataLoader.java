package cz.zcu.sar.centraldb.client.core.utils;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.client.persistence.services.AddressService;
import cz.zcu.sar.centraldb.client.persistence.services.AddressTypeService;
import cz.zcu.sar.centraldb.client.persistence.services.PersonService;
import cz.zcu.sar.centraldb.client.persistence.services.PersonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Matej Lochman on 5.1.17.
 */
@Component
public class TestDataLoader {
    @Autowired
    private PersonService personService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PersonTypeService personTypeService;
    @Autowired
    private AddressTypeService addressTypeService;

    private PersonType createPersonType(String description) {
        PersonType personType = new PersonType();
        personType.setDescription(description);
        personType.setModifiedBy("init");
        personTypeService.save(personType);
        return personType;
    }

    private List<PersonType> createPersonTypes() {
        List<PersonType> personTypes = new LinkedList<>();
        personTypes.add(createPersonType("Fyzická osoba"));
        personTypes.add(createPersonType("Právnická osoba"));
        return personTypes;
    }

    private AddressType createAddressType(String description) {
        AddressType addressType = new AddressType();
        addressType.setDescription(description);
        addressType.setModifiedBy("init");
        addressTypeService.save(addressType);
        return addressType;
    }

    private List<AddressType> createAddressTypes() {
        List<AddressType> addressTypes = new LinkedList<>();
        addressTypes.add(createAddressType("Trvalá adresa"));
        addressTypes.add(createAddressType("Přechodná adresa"));
        return addressTypes;
    }

    public void generatePeople(int count) {
        List<PersonType> personTypes = createPersonTypes();
        List<AddressType> addressTypes = createAddressTypes();
        Person person;
        DataGenerator generator = new DataGenerator(Person.class, Address.class);
        for (int i = 0; i < count; i++) {
            //company or person
            if (ThreadLocalRandom.current().nextInt(2) == 0) {
                person = generator.nextPerson(personTypes.get(0));
            } else {
                person = generator.nextCompany(personTypes.get(1));
            }
            person = personService.save(person);
            Set<Address> addresses = new HashSet<>();
            //address
            Address address = generator.nextAddress(addressTypes.get(0));
            address.setPerson(person);
            addressService.save(address);
            addresses.add(address);
            //temporary address
            if (ThreadLocalRandom.current().nextInt(10) == 0) {
                Address address2 = generator.nextAddress(addressTypes.get(1));
                address2.setPerson(person);
                addressService.save(address2);
                addresses.add(address2);
            }
            person.setAddressWrappers(addresses);
            personService.save(person);
        }
    }
}

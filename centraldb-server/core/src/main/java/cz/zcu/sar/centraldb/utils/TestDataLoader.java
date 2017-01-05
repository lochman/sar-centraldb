package cz.zcu.sar.centraldb.utils;

import cz.zcu.sar.centraldb.persistence.domain.*;
import cz.zcu.sar.centraldb.persistence.service.*;
import cz.zcu.sar.centraldb.synchronization.SyncQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Matej Lochman on 22.12.16.
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
    @Autowired
    private InstituteService instituteService;

    private final Map<Long, String> institutes = new HashMap<Long, String>(){{
        put(0L, "Praha");
        put(1L, "Plzen");
        put(2L, "Ostrava");
    }};

    public void initInstitutes() {
        for (Map.Entry<Long, String> entry : institutes.entrySet()) {
            instituteService.save(new Institute(entry.getKey(), entry.getValue()));
        }
    }

    private PersonType createPersonType(String description) {
        PersonType personType = new PersonType();
        personType.setDescription(description);
        personType.setModifiedBy("init");
        personType = personTypeService.save(personType);
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
        addressType = addressTypeService.save(addressType);
        return addressType;
    }

    private List<AddressType> createAddressTypes() {
        List<AddressType> addressTypes = new LinkedList<>();
        addressTypes.add(createAddressType("Trvalá adresa"));
        addressTypes.add(createAddressType("Přechodná adresa"));
        return addressTypes;
    }

    public void generatePeople(boolean temp, int count) {
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
            person.setTemporary(temp);
            person = personService.save(person);
            Set<Address> addresses = new HashSet<>();
            //address
            Address address = generator.nextAddress(addressTypes.get(0));
            address.setPerson(person);
            address = addressService.save(address);
            addresses.add(address);
            //temporary address
            if (ThreadLocalRandom.current().nextInt(10) == 0) {
                Address address2 = generator.nextAddress(addressTypes.get(1));
                address2.setPerson(person);
                address2 = addressService.save(address2);
                addresses.add(address2);
            }
            person.setAddressWrappers(addresses);
            personService.save(person);
        }
    }
}

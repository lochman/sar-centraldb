package cz.zcu.sar.centraldb.utils;

import cz.zcu.sar.centraldb.common.utils.BasicTestDataGenerator;
import cz.zcu.sar.centraldb.common.utils.TestDataGenerator;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;

/**
 * Created by Matej Lochman on 5.1.17.
 */
public class DataGenerator extends BasicTestDataGenerator<Person, Address, PersonType, AddressType> implements TestDataGenerator<Person, Address, PersonType, AddressType> {
    DataGenerator(Class<Person> personClass, Class<Address> addressClass) {
        super(personClass, addressClass);
    }
}

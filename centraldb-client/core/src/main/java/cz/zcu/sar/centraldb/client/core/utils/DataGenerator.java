package cz.zcu.sar.centraldb.client.core.utils;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.common.utils.BasicTestDataGenerator;
import cz.zcu.sar.centraldb.common.utils.TestDataGenerator;

/**
 * Created by Matej Lochman on 5.1.17.
 */
public class DataGenerator extends BasicTestDataGenerator<Person, Address, PersonType, AddressType>
        implements TestDataGenerator<Person, Address, PersonType, AddressType> {
    DataGenerator(Class<Person> personClass, Class<Address> addressClass) {
        super(personClass, addressClass);
    }
}

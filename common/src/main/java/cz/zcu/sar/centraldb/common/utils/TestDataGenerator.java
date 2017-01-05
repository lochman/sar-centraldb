package cz.zcu.sar.centraldb.common.utils;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;

/**
 * Created by Matej Lochman on 5.1.17.
 */

public interface TestDataGenerator<P extends PersonWrapper, A extends AddressWrapper,
        PT extends PersonTypeWrapper, AT extends AddressTypeWrapper> {
    P nextPerson(PT personType);
    P nextPerson(PT personType, boolean male);
    P nextCompany(PT personType);
    A nextAddress(AT addressType);
}

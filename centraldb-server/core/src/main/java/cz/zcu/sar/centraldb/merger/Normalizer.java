package cz.zcu.sar.centraldb.merger;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.persistence.domain.Person;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
public interface Normalizer {
    Person normalize(PersonWrapper<PersonTypeWrapper,AddressWrapper>  wrapper);
}

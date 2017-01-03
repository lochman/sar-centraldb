package cz.zcu.sar.centraldb.merger;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.persistence.domain.Person;

import java.util.List;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
public interface Normalizer {
    /**
     * normalize from personWrapper into person
     * @param wrapper wrapper
     * @return person
     */
    Person normalize(PersonWrapper<PersonTypeWrapper, AddressWrapper> wrapper);
    /**
     * normalize from personWrapper into person
     * @param personWrappers wrapper
     * @return list of person
     */
    List<Person> normalize(PersonWrapper[] personWrappers);
}

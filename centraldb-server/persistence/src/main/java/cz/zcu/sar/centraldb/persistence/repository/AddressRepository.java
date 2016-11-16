package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Matej Lochman on 31.10.16.
 */
public interface AddressRepository extends CrudRepository<Address, Long> {

    Address findByPerson(Person person);
}

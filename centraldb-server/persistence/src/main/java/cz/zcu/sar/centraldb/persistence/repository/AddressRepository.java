package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;

import java.util.Optional;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface AddressRepository extends BaseRepository<Address, Long> {

    Optional<Address> findByPerson(Person person);
}

package cz.zcu.sar.centraldb.client.persistence.repository;


import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;

import java.util.Optional;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface AddressRepository extends BaseRepository<Address, String> {

    Optional<Address> findByPerson(Person person);
}

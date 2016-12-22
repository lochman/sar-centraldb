package cz.zcu.sar.centraldb.client.persistence.repository;


import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface AddressRepository extends BaseRepository<Address, String> {

    List<Address> findByPerson(Person person);

    @Query("select a.addressType from Address a where a.id=:id")
    AddressType findAddressType(@Param("id")Long id );
}

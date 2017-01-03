package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.common.persistence.repository.BaseRepository;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface AddressRepository extends BaseRepository<Address, Long> {

    Optional<Address> findByPerson(Person person);
    @Query("select a.addressType from Address a where a.id=:id")
    AddressType findAddressType(@Param("id")Long id );
    @Query("select a from Address a where a.person=:person")
    List<Address> findByPersonToList(@Param("person")Person person);
}

package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface PersonRepository extends BaseRepository<Person, Long> {
    String FIND_BY_DATE = "SELECT p FROM Person p WHERE p.modifiedTime > :timeStart ORDER BY p.modifiedTime";

    @Query(FIND_BY_DATE)
    Page<Person> findByDate(@Param("timeStart") Timestamp timeStart, Pageable page);
    @Query(FIND_BY_DATE)
    List<Person> findByDate(@Param("timeStart") Timestamp timeStart);

    Optional<Person> findByName(String name);

    @Query("SELECT p.addressWrappers FROM Person p WHERE p.id = :id")
    Set<Address> getAddresses(@Param("id") Long personId);
}

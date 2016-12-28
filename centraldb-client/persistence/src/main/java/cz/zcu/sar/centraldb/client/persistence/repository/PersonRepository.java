package cz.zcu.sar.centraldb.client.persistence.repository;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 31.10.16.
 */

public interface PersonRepository extends BaseRepository<Person, Long> {
    String FIND_BY_TIME = "SELECT p " +
            "FROM Person p WHERE p.modifiedTime > :timeStart AND p.modifiedTime <= :timeEnd AND p.modifiedBy <> 'AUTO'";

    Optional<Person> findByName(String name);
    @Query(FIND_BY_TIME)
    List<Person> findByDate(@Param("timeStart")Timestamp timeStart,
                            @Param("timeEnd")Timestamp timeEnd);

    @Query("select p.personType from Person p where p.id=:id")
    PersonType findPersonType(@Param("id")Long id );

    List<Person> findBySocialNumber(String socialNumber);
    List<Person> findByCompanyNumber(String companyNumber);
    List<Person> findBySocialNumberAndCompanyNumber(String socialNumber,String companyNumber);




}

package cz.zcu.sar.centraldb.client.persistence.repository;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
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


}

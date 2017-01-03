package cz.zcu.sar.centraldb.persistence.repository;

import cz.zcu.sar.centraldb.common.persistence.repository.BaseRepository;
import cz.zcu.sar.centraldb.persistence.domain.Institute;

import java.util.Optional;

/**
 * Created by Matej Lochman on 17.11.16.
 */

public interface InstituteRepository extends BaseRepository<Institute, Long> {
    Optional<Institute> findByName(String name);
}

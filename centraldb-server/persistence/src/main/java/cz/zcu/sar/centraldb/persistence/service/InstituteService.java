package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.persistence.domain.Institute;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Matej Lochman on 28.12.16.
 */

public interface InstituteService {
    List<Institute> findAll();
    boolean updateSyncOut(Long instituteId, Timestamp syncOut);
}

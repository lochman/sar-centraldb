package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseService;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 28.12.16.
 */

@Transactional
public interface InstituteService extends BaseService<Institute, Long> {
    void updateSyncOut(Long instituteId, Timestamp syncOut);
    void updateBatchId(String clientId, String batchId);
}

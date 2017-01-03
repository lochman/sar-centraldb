package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseService;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Matej Lochman on 28.12.16.
 */

@Transactional
public interface InstituteService extends BaseService<Institute, Long> {
    boolean updateSyncOut(Long instituteId, Timestamp syncOut);
    void updateBatchId(Institute institute, String batchId);
}

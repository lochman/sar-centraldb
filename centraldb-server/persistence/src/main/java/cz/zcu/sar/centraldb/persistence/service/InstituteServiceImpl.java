package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import cz.zcu.sar.centraldb.common.synchronization.Batch;
import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.repository.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Created by Matej Lochman on 28.12.16.
 */

@Service
public class InstituteServiceImpl extends BaseServiceImpl<Institute, Long, InstituteRepository> implements InstituteService {

    @Autowired
    InstituteRepository instituteRepository;

    @Override
    public void updateSyncOut(Long instituteId, Timestamp syncOut) {
        instituteRepository.findOne(instituteId)
                .ifPresent(institute -> institute.setLastSyncOut(syncOut));
        logger.info("updateSyncOut: id=" + instituteId + ", syncOut=" + syncOut);

    }
    @Override
    public void updateBatchId(String clientId, String batchId) {
        try {
            Optional<Institute> institute = instituteRepository.findOne(Long.parseLong(clientId));
            institute.ifPresent(theInstitute -> {
                theInstitute.setLastBatchId(batchId);
                instituteRepository.save(theInstitute);
                logger.info("updateBatchId: batchId='" + batchId + "', clientId=" + clientId + ", " + theInstitute);
            });
            if (!institute.isPresent()) {
                logger.warn("updateBatchId: failed to find institute with id {}", clientId);
            }
        } catch (NumberFormatException e) {
            logger.warn("updateBatchId: failed to parse institute id {}", clientId);
        }
    }
}

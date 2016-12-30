package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.persistence.domain.Institute;
import cz.zcu.sar.centraldb.persistence.repository.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by Matej Lochman on 28.12.16.
 */

@Service
public class InstituteServiceImpl implements InstituteService {

    @Autowired
    InstituteRepository instituteRepository;

    @Override
    public List<Institute> findAll() {
        return instituteRepository.findAll(null);
    }

    @Override
    public boolean updateSyncOut(Long instituteId, Timestamp syncOut) {
        Optional<Institute> institute = instituteRepository.findOne(instituteId);
        if (institute.isPresent()) {
            institute.get().setLastSyncOut(syncOut);
            return true;
        }
        return false;
    }
}
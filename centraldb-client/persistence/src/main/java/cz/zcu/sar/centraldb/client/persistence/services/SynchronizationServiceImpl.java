package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Synchronization;
import cz.zcu.sar.centraldb.client.persistence.repository.SynchronizationRepository;
import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
@Service
@Transactional
public class SynchronizationServiceImpl extends BaseServiceImpl<Synchronization,Long,SynchronizationRepository> implements SynchronizationService {
    @Autowired
    SynchronizationRepository synchronizationRepository;

    public Synchronization findLast() {
        List<Synchronization> syncs = findAll();
        Synchronization max = null;
        if (!syncs.isEmpty()) {
            Iterator<Synchronization> it = syncs.iterator();
            while (it.hasNext()) {
                Synchronization next = it.next();
                if (max == null || max.getId() < next.getId()) max = next;
            }
        }
        return max;
    }
}

package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressTypeRepository;
import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
@Service
public class AddressTypeServiceImpl extends BaseServiceImpl<AddressType, Long, AddressTypeRepository> implements AddressTypeService {
    public AddressType findAddressType(Long id) {
        Optional<AddressType> res = findOne(id);
        if (res.isPresent()) {
            return res.get();
        }
        return null;
    }
}

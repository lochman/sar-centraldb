package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.common.persistence.service.BaseService;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
public interface AddressTypeService extends BaseService<AddressType, Long> {
    /**
     * find adress type by id
     * @param id id
     * @return addressType
     */
    AddressType findAddressType(Long id);
}

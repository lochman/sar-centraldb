package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseService;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Matej Lochman on 2.1.17.
 */

@Transactional
public interface AddressService extends BaseService<Address, Long> {
}

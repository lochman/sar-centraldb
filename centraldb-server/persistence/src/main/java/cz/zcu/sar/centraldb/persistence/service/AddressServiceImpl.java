package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.persistence.repository.AddressTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Matej Lochman on 2.1.17.
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, Long, AddressRepository> implements AddressService{
}

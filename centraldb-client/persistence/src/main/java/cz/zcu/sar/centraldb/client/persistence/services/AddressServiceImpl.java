package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
@Service
@Transactional
public class AddressServiceImpl extends BaseServiceImpl<Address, Long, AddressRepository> implements AddressService {

}



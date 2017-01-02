package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.repository.AddressTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Matej Lochman on 2.1.17.
 */
@Service
public class AddressTypeServiceImpl extends BaseServiceImpl<AddressType, Long, AddressTypeRepository> implements AddressTypeService{
}

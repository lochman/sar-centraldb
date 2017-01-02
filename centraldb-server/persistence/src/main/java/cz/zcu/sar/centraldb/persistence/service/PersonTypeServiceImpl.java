package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.repository.PersonTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Matej Lochman on 2.1.17.
 */
@Service
public class PersonTypeServiceImpl extends BaseServiceImpl<PersonType, Long, PersonTypeRepository> implements PersonTypeService{
}

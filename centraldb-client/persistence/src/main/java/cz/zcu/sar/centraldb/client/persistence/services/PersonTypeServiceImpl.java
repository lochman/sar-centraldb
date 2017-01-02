package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonTypeRepository;
import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
@Service
public class PersonTypeServiceImpl extends BaseServiceImpl<PersonType,Long,PersonTypeRepository> implements PersonTypeService {
    @Autowired
    PersonTypeRepository personTypeRepository;

    @Override
    public PersonType mergePersonType(PersonType personType) {
        if (personType==null) return null;
        Optional<PersonType> res = findOne(personType.getId());
        if (res.isPresent()) {
            return res.get();
        }
        return null;
    }
}

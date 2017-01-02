package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.common.persistence.service.BaseService;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
public interface PersonTypeService extends BaseService<PersonType,Long> {
    PersonType mergePersonType(PersonType personType);
}

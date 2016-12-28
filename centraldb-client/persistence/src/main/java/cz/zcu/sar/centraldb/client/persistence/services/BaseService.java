package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.common.persistence.BaseObject;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
public interface BaseService {
    /**
     * Set value as last modified time and modified by
     * @param baseObject object
     * @param isSync auto is a robot
     * @return base object
     */
    BaseObject setDefaultValue(BaseObject baseObject, boolean isSync);

    /**
     * set modified by
     * @param baseObject object
     * @param isSync auto is a robot
     * @return base object
     */
    BaseObject setModifyBy(BaseObject baseObject,boolean isSync);

    /**
     * fill all lazy attributes
     * @param persons person
     * @return fill person
     */
    Person fillLazyAttribute(Person persons);
}

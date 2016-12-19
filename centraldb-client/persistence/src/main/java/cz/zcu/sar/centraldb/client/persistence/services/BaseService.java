package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.BaseObject;

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
}

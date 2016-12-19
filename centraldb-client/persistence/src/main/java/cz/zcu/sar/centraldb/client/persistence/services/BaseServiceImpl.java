package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.BaseObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class BaseServiceImpl implements BaseService{
    private static String AUTO = "AUTO";
    private static String USER = "USER";

    @Override
    public BaseObject setDefaultValue(BaseObject baseObject, boolean isSync) {
        if (isSync){
            baseObject.setModifiedBy(AUTO);
        }else{
            baseObject.setModifiedBy(USER);
        }
        baseObject.setModifiedTime( new Timestamp(System.currentTimeMillis()));
        return baseObject;
    }
}

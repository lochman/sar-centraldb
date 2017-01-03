package cz.zcu.sar.centraldb.merger;

import cz.zcu.sar.centraldb.common.persistence.domain.BaseObject;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class UtilServiceImpl implements UtilService {
    private static String AUTO = "AUTO";
    private static String USER = "USER";
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AddressRepository addressRepository;

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
    public BaseObject setModifyBy(BaseObject baseObject,boolean isSync){
        if (isSync){
            baseObject.setModifiedBy(AUTO);
        }else{
            baseObject.setModifiedBy(USER);
        }
        return baseObject;
    }

}

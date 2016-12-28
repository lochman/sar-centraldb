package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.common.persistence.BaseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class BaseServiceImpl implements BaseService{
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
    public Person fillLazyAttribute(Person persons) {
        PersonType type = personRepository.findPersonType(persons.getId());
        List<Address> addresses = addressRepository.findByPerson(persons);
        for (Address a : addresses){
            AddressType addressType = addressRepository.findAddressType(a.getId());
            a.setAddressType(addressType);
            a.setPerson(persons);
        }
        persons.setAddressWrappers(new HashSet<>(addresses));
        persons.setPersonType(type);
        return persons;
    }
}

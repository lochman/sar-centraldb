package cz.zcu.sar.centraldb.merger;

import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonTypeWrapper;
import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.service.AddressTypeService;
import cz.zcu.sar.centraldb.persistence.service.PersonTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
@Service
public class NormalizerImpl implements Normalizer {
    @Autowired
    private PersonTypeService personTypeService;
    @Autowired
    private AddressTypeService addressTypeService;

    @Override
    public Person normalize(PersonWrapper<PersonTypeWrapper, AddressWrapper> wrapper) {
        Person person = new Person();
        BeanUtils.copyProperties(wrapper, person);
        Set<Address> addresses= new HashSet<>();
        if (wrapper.getAddressWrappers()==null) wrapper.setAddressWrappers(new HashSet<>());
        for (AddressWrapper a : wrapper.getAddressWrappers()){
            Address add = normalizeAddress(a);
            add.setPerson(person);
            addresses.add(add);
        }
        person.setAddressWrappers(addresses);
        PersonType personType = new PersonType();
        BeanUtils.copyProperties(wrapper.getPersonType(), personType);
        person.setPersonType(personTypeService.mergePersonType(personType));
//        person.setId(person.getForeignId());
//        person.setForeignId(person.getId());
        return person;
    }

    @Override
    public List<Person> normalize(PersonWrapper[] personWrappers) {
        List<Person> normalized = new LinkedList<>();
        for (PersonWrapper personWrapper : personWrappers) {
            normalized.add(normalize(personWrapper));
        }
        return normalized;
    }

    /**
     * normalize address
     * @param wrapper address wrapper
     * @return address
     */
    private Address normalizeAddress(AddressWrapper wrapper){
        Address address = new Address();
        BeanUtils.copyProperties(wrapper, address);
        AddressType type = new AddressType();
        BeanUtils.copyProperties(wrapper.getAddressType(), type);
        address.setAddressType(addressTypeService.findAddressType(type.getId()));
        return address;
    }
}

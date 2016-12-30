package cz.zcu.sar.centraldb.client.Merger;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.client.persistence.repository.*;
import cz.zcu.sar.centraldb.client.persistence.services.BaseService;
import cz.zcu.sar.centraldb.client.persistence.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marek Rasocha
 *         date 16.12.2016.
 */
@Service
public class MergerImpl implements Merger {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonTypeRepository personTypeRepository;

    @Autowired
    AddressTypeRepository addressTypeRepository;

    @Autowired
    BaseService baseService;

    @Autowired
    PersonService personService;

    @Override
    public boolean mergeData(List<Person> persons){
        for (Person p : persons){
            Person save = null;
            if (p.getId()!=null)save = personRepository.findOne(p.getId());
            // IF person is not founded, Try to find by companyNumber or socialNumber
            if(save==null) save = mergePerson(p);
            if (save!=null){
                //TODO - uncomment after testing
//                if(save.getModifiedTime().after(p.getModifiedTime()))continue;
                save = baseService.fillLazyAttribute(save);
                p.setId(save.getId());
                if(save.getAddressWrappers()==null) save.setAddressWrappers(new HashSet<>());
                if(p.getAddressWrappers()==null) p.setAddressWrappers(new HashSet<>());
                // null id and merge address
                p.setAddressWrappers(mergeAddress(new ArrayList(p.getAddressWrappers()), new ArrayList(save.getAddressWrappers())));
            }
            p.setPersonType(mergePersonType(p.getPersonType()));
            p = (Person)baseService.setModifyBy(p,true);
            personService.createPerson(p);
        }
        return true;
    }

    private PersonType mergePersonType(PersonType personType) {
        return personTypeRepository.findOne(personType.getId());
    }

    private Person mergePerson(Person p) {
        List<Person> result = new ArrayList<>();
        if (p.getSocialNumber()!=null && p.getCompanyNumber()!=null){
            result.addAll(personRepository.findBySocialNumberAndCompanyNumber(p.getSocialNumber(), p.getCompanyNumber()));
        }else{
            if (p.getSocialNumber()!=null){
                result.addAll(personRepository.findBySocialNumber(p.getSocialNumber()));
            }
            if(p.getCompanyNumber()!=null){
                result.addAll(personRepository.findByCompanyNumber(p.getCompanyNumber()));
            }
        }
        Person search = null;
        if (!result.isEmpty()){
            search = result.get(0);
        }
        return search;
    }

    private Set<Address> mergeAddress(List<cz.zcu.sar.centraldb.common.persistence.Address> addressNew, List<Address> addressOld) {
        Set<Address> address = new HashSet<>();
        for (cz.zcu.sar.centraldb.common.persistence.Address newA : addressNew){
            Address address1 = new Address(newA);
            int index = getAddress(address1, addressOld);
            if (index!=-1){
                Address old = addressOld.remove(index);
                address.add(old);
            }else{
                address1.setAddressType(addressTypeRepository.findOne(address1.getAddressType().getId()));
                address.add(address1);
            }
        }
        address.addAll(addressOld);
        return address;
    }

    private int getAddress(Address address, List<Address> addresses) {
        int i=0;
        for (Address a : addresses){
            if (a.equals(address)) return i;
            i++;
        }
        //address.setId(null);        // null id -> hibernate save as a new row
        return -1;
    }
}

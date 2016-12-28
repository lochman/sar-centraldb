package cz.zcu.sar.centraldb.client.Merger;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.SynchronizationRepository;
import cz.zcu.sar.centraldb.client.persistence.services.BaseService;
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
    BaseService baseService;

    @Override
    public boolean mergeData(List<Person> persons){
        for (Person p : persons){
            Person save = null;
            if (p.getId()!=null){
                save = personRepository.findOne(p.getId());
                if (save!=null)save = baseService.fillLazyAttribute(save);
            }
            if(save!=null){
                if(save.getModifiedTime().after(p.getModifiedTime()))continue;
            }else{
                save = mergePerson(p);
                if (save!=null)save = baseService.fillLazyAttribute(save);
            }
            if (save!=null){
                // null id and merge address
                p.setAddressWrappers(mergeAddress(new ArrayList(p.getAddressWrappers()),new ArrayList(save.getAddressWrappers())));
            }
            p = (Person)baseService.setModifyBy(p,true);
            personRepository.save(p);
        }
        return true;
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

    private Set<Address> mergeAddress(List<Address> addressNew, List<Address> addressOld) {
        Set<Address> address = new HashSet<>();
        for (Address newA : addressNew){
            int index = getAddress(newA, addressOld);
            if (index!=-1){
                Address old = addressOld.remove(index);
                address.add(old);
            }else{
                newA.setId(null);
                address.add(newA);
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

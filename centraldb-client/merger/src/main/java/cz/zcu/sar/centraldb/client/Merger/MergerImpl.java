package cz.zcu.sar.centraldb.client.Merger;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.services.AddressTypeService;
import cz.zcu.sar.centraldb.client.persistence.services.PersonService;
import cz.zcu.sar.centraldb.client.persistence.services.PersonTypeService;
import cz.zcu.sar.centraldb.client.persistence.services.UtilService;
import cz.zcu.sar.centraldb.common.persistence.domain.AddressWrapper;
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
    AddressTypeService addressTypeService;
    @Autowired
    PersonService personService;
    @Autowired
    PersonTypeService personTypeService;
    @Autowired
    UtilService utilService;


    @Override
    public boolean mergeData(List<Person> persons){
        for (Person p : persons){
            Person save = p.getId()==null ? null : personService.findPerson(p.getId());
           // IF person is not founded, Try to find by companyNumber or socialNumber
            if(save==null) save = personService.mergePerson(p);
            if (save!=null){
                //TODO - uncomment after testing
//                if(save.getModifiedTime().after(p.getModifiedTime()))continue;
                save = utilService.fillLazyAttribute(save);
                p.setId(save.getId());
                if(save.getAddressWrappers()==null) save.setAddressWrappers(new HashSet<>());
                if(p.getAddressWrappers()==null) p.setAddressWrappers(new HashSet<>());
                // null id and merge address
                p.setAddressWrappers(mergeAddress(new ArrayList(p.getAddressWrappers()), new ArrayList(save.getAddressWrappers())));
            }
            p.setPersonType(personTypeService.mergePersonType(p.getPersonType()));
            p = (Person)utilService.setModifyBy(p,true);
            personService.createPerson(p);
        }
        return true;
    }


    private Set<Address> mergeAddress(List<AddressWrapper> addressNew, List<Address> addressOld) {
        Set<Address> address = new HashSet<>();
        for (AddressWrapper newA : addressNew){
            Address address1 = new Address(newA);
            int index = getAddress(address1, addressOld);
            if (index!=-1){
                Address old = addressOld.remove(index);
                address.add(old);
            }else{
                address1.setAddressType(addressTypeService.findAddressType(address1.getAddressType().getId()));
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

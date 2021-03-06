package cz.zcu.sar.centraldb.merger;


import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.AddressService;
import cz.zcu.sar.centraldb.persistence.service.AddressTypeService;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import cz.zcu.sar.centraldb.persistence.service.PersonTypeService;
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
    @Autowired
    AddressService addressService;

    @Override
    public Person mergeData(Person temporal, Person persist){
        if (persist!=null){
          if(persist.getModifiedTime().after(temporal.getModifiedTime()))return null;
            persist = personService.fillLazyAttribute(persist);
            if(persist.getAddressWrappers()==null) persist.setAddressWrappers(new HashSet<>());
            if(temporal.getAddressWrappers()==null) temporal.setAddressWrappers(new HashSet<>());
            // null id and merge address
            temporal.setAddressWrappers(mergeAddress(temporal,new ArrayList<>(temporal.getAddressWrappers()), new ArrayList<>(persist.getAddressWrappers())));
            temporal.setLookupOk(true);
            temporal.setTemporary(false);
        }else{
            temporal.setLookupOk(false);
            temporal.setTemporary(true);
        }
        temporal = (Person)utilService.setModifyBy(temporal,true);
        temporal = personService.savePersonWithAddresses(temporal);
        if (persist!=null){
            persist.setAddressWrappers(new HashSet<>());
            personService.delete(persist.getId());
        }
        return temporal;
    }

    /**
     * merge adress
     *
     * @param temporal person
     * @param addressNew address new
     * @param addressOld address old
     * @return
     */
    private Set<Address> mergeAddress(Person temporal, List<Address> addressNew, List<Address> addressOld) {
        Set<Address> address = new HashSet<>();
        List<Address> removed = new ArrayList<>();
        for (Address address1 : addressNew){
            int index = getAddress(address1, addressOld);
            if (index!=-1){
                Address old = addressOld.remove(index);
                old.setPerson(temporal);
                address.add(old);
                if (!address1.getId().equals(old.getId())) {
//                    address1.setAddressType(null);
//                    address1.setPerson(null);
                    removed.add(address1);
                }
            }else{
                address1.setPerson(temporal);
                address.add(address1);
            }
        }
        addressService.delete(removed);
        address.addAll(addressOld);
        return address;
    }


    /**
     * search address in addresses
     * @param address address
     * @param addresses addresses
     * @return index
     */
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

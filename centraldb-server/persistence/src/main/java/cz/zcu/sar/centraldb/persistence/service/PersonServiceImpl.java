package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.domain.PersonWrapper;
import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.AddressType;
import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.domain.PersonType;
import cz.zcu.sar.centraldb.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.persistence.wrapper.PageRequestWrapper;
import cz.zcu.sar.centraldb.persistence.wrapper.PersonAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static cz.zcu.sar.centraldb.persistence.specification.PersonSpecifications.hasProperties;

/**
 * Created by Matej Lochman on 21.12.16.
 */

@Service
public class PersonServiceImpl extends BaseServiceImpl<Person, Long, PersonRepository> implements PersonService {

    private static final int DEFAULT_PAGE_LIMIT = 20;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<PersonWrapper> getUnsynchronized(Timestamp from) {
        List<PersonWrapper> normalized = new LinkedList<>();
        List<Person> unSync = personRepository.findByDate(from);
        for (Person person : unSync) {
            person = fillLazyAttribute(person);
            normalized.add(person.getPersonWrapper());
        }
//        logger.debug("getUnsynchronized: from=" + from + ", listSize=" + unsync.size());
        return normalized;
    }

    @Override
    public Page<Person> getPeopleByQuery(PageRequestWrapper requestWrapper) {
        PageRequestWrapper.PaginationParams queryParams = requestWrapper.getPaginationParams();
        Integer page = queryParams.getPage(), limit = queryParams.getLimit();
        page = (page == null) ? 0 : page;
        limit = (limit == null) ? DEFAULT_PAGE_LIMIT : limit;
        PageRequest pageRequest;
        if (queryParams.getSort() != null) {
            pageRequest = new PageRequest(page, limit, Sort.Direction.ASC,
                    queryParams.getSort().toArray(new String[queryParams.getSort().size()]));
        } else {
            pageRequest = new PageRequest(page, limit);
        }
        Page<Person> all =  personRepository.findAll(hasProperties(requestWrapper.getQueryParams()), pageRequest);
        logger.debug("getPeopleByQuery: requestWrapper=" + requestWrapper + ", pageSize=" + all.getNumberOfElements());
        return all;
    }

    @Override
    public Person savePersonAsTemp(Person person) {
        person.setTemporary(true);
        return save(person);
    }

    @Override
    public Person createPerson(PersonAddress personAddress, String modifiedBy) {
        Person person = personAddress.getPerson();
        person.setModifiedBy(modifiedBy);
        savePersonAsTemp(person);
        if(personAddress.getAddressWrappers().length > 0) {
            saveAddress(person, personAddress, modifiedBy);
        }
        return person;
    }

    @Override
    public void saveAddress(Person person, PersonAddress personAddress, String modifiedBy) {
        Set<Address> addresses = personRepository.getAddresses(person.getId());
        for (Address address : personAddress.getAddressWrappers()) {
            address.setPerson(person);
            address.setModifiedBy(modifiedBy);
            addressRepository.save(address);
            addresses.add(address);
        }
        person.setAddressWrappers(addresses);
        save(person);
        logger.info("saveAddress: modifiedBy='" + modifiedBy + "', " + person + ", " + personAddress);
    }

    public Person findPersonByNumbers(String socialNumber, String companyNumber){
        List<Person> result = new ArrayList<>();
        if (socialNumber!=null && companyNumber!=null){
            result.addAll(personRepository.findBySocialNumberAndCompanyNumberAndTemporary(socialNumber, companyNumber, false));
        }else{
            if (socialNumber!=null){
                result.addAll(personRepository.findBySocialNumberAndTemporary(socialNumber, false));
            }
            if(companyNumber!=null){
                result.addAll(personRepository.findByCompanyNumberAndTemporary(companyNumber, false));
            }
        }
        Person search = null;
        if (!result.isEmpty()) search = result.get(0);

        logger.debug("findPersonByNumbers: socialNumber=" + socialNumber + ", companyNumber=" + companyNumber + ", personFound=" + search);
        return search;
    }

    @Override
    @Transactional
    public Person createPerson(Person person) {
        person = save(person);
        if (person.getAddressWrappers()!=null){
            for (Address a : person.getAddressWrappers()){
                a.setPerson(person);
                addressRepository.save(a);
                person.getAddressWrappers().forEach(addressRepository::save);
            }
        }
        return person;
    }

    public Person findPerson(Long id){
        if (id==null) return null;
        Optional<Person> res = findOne(id);
        if (res.isPresent()) {
            return res.get();
        }
        return null;
    }

    public Person fillLazyAttribute(Person person) {
        PersonType type = personRepository.findPersonType(person.getId());
//        Optional<Address> optional = addressRepository.findByPerson(persons);
        List<Address> addresses =  addressRepository.findByPersonToList(person);
        for (Address a : addresses){
            AddressType addressType = addressRepository.findAddressType(a.getId());
            a.setAddressType(addressType);
            a.setPerson(person);
        }
        person.setAddressWrappers(new HashSet<>(addresses));
        person.setPersonType(type);
        logger.debug("fillLazyAttribute: " + person);

        return person;
    }
    public List<Person> initMergeBuffer(){
        List<Person> lazy = personRepository.findByTemporaryAndLookupOk(true, true);

         return (lazy.stream().map(this::fillLazyAttribute).collect(Collectors.toList()));

    }


}

package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import cz.zcu.sar.centraldb.persistence.domain.Address;
import cz.zcu.sar.centraldb.persistence.domain.Person;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public List<Person> getUnsynchronized(Timestamp from) {
        return personRepository.findByDate(from);
    }

    public List<Person> getUnsynchronized(Timestamp from, int count) {
        return personRepository.findByDate(from, new PageRequest(0, count)).getContent();
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
        return personRepository.findAll(hasProperties(requestWrapper.getQueryParams()), pageRequest);
    }

    @Override
    public Person savePersonAsTemp(Person person) {
//        System.out.println("person before save " + person.getName() + " " + person.getId());
        person.setTemporary(true);
        person = personRepository.save(person);
//        System.out.println("person after save " + person.getName() + " " + person.getId());
        return person;
    }

    @Override
    public Person createPerson(PersonAddress personAddress, String modifiedBy) {
        Person person = personAddress.getPerson();
        person.setModifiedBy(modifiedBy);
        System.out.println(person.getBirthDate().getTime());
        savePersonAsTemp(person);
        saveAddress(person, personAddress, modifiedBy);
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
        return search;
    }

    @Override
    @Transactional
    public void createPerson(Person person) {
        personRepository.save(person);
        if (person.getAddressWrappers()!=null){
            person.getAddressWrappers().forEach(addressRepository::save);
        }
    }


}

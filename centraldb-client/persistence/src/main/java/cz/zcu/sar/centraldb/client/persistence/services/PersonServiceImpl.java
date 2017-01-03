package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressTypeRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonTypeRepository;
import cz.zcu.sar.centraldb.common.persistence.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rasocha
 *         date 28.12.2016.
 */
@Service
@Transactional
public class PersonServiceImpl extends BaseServiceImpl<Person, Long, PersonRepository> implements PersonService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    PersonTypeRepository personTypeRepository;
    @Autowired
    AddressTypeRepository addressTypeRepository;


    @Override
    @Transactional
    public void createPerson(Person person) {
        personRepository.save(person);
        if (person.getAddressWrappers()!=null){
            person.getAddressWrappers().forEach(addressRepository::save);
        }
    }
    public Person mergePerson(Person p) {
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
    public Person findPerson(Long id){
        if (id==null) return null;
        Optional<Person> res = findOne(id);
        if (res.isPresent()) {
            return res.get();
        }
        return null;
    }

    @Override
    public List<Person> findPersonByDate(Timestamp startDate, Timestamp endDate) {
        return personRepository.findByDate(startDate, endDate);
    }
    public Person findPersonByGlobalId(Long id){
        return personRepository.findByCentralId(id);
    }
}

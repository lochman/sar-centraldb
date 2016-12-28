package cz.zcu.sar.centraldb.client.persistence.services;

import cz.zcu.sar.centraldb.client.persistence.domain.Address;
import cz.zcu.sar.centraldb.client.persistence.domain.Person;
import cz.zcu.sar.centraldb.client.persistence.repository.AddressRepository;
import cz.zcu.sar.centraldb.client.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Marek Rasocha
 *         date 28.12.2016.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService{

    @Autowired
    PersonRepository personRepository;
    @Autowired
    AddressRepository addressRepository;
    @Override
    @Transactional
    public void createPerson(Person person) {
        personRepository.save(person);
        if (person.getAddressWrappers()!=null){
            person.getAddressWrappers().forEach(addressRepository::save);
        }
    }
}

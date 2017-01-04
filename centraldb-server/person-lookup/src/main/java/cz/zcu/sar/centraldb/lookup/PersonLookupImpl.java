package cz.zcu.sar.centraldb.lookup;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
@Service
public class PersonLookupImpl implements PersonLookup {


    @Autowired
    PersonService personService;

    @Override
    public Person findPerson(Person person) {
        if (person == null) return null;
        return personService.findPersonByNumbers(person.getSocialNumber(), person.getCompanyNumber());
    }
}

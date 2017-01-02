package cz.zcu.sar.centraldb.lookup;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marek Rasocha
 *         date 02.01.2017.
 */
public class LookupPersonImpl implements LookupPerson {

    @Autowired
    PersonService personService;
    @Override
    public Person findPerson(Person person) {
        if(person==null) return null;
        return personService.findPersonByNumbers(person.getSocialNumber(),person.getCompanyNumber());
    }
}

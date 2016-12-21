package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static cz.zcu.sar.centraldb.persistence.specification.PersonSpecifications.hasProperties;

/**
 * Created by Matej Lochman on 21.12.16.
 */

public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Page<Person> getPeopleByQuery(PageRequestWrapper requestWrapper) {
        PageRequestWrapper.PagingParams queryParams = requestWrapper.getPagingParams();
        PageRequest pageRequest = new PageRequest(queryParams.getPage(), queryParams.getLimit(), Sort.Direction.ASC,
                queryParams.getSort().toArray(new String[queryParams.getSort().size()]));
        return personRepository.findAll(hasProperties(requestWrapper.getQueryParams()), pageRequest);
    }

    @Override
    public void savePersonAsTemp(Person person) {
        System.out.println("person before save " + person.getName() + " " + person.getId());
        person.setTemporary(true);
        personRepository.save(person);
        System.out.println("person after save " + person.getName() + " " + person.getId());
    }
}

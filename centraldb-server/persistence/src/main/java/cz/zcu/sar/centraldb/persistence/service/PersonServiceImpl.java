package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static cz.zcu.sar.centraldb.persistence.specification.PersonSpecifications.hasProperties;

/**
 * Created by Matej Lochman on 21.12.16.
 */

@Service
public class PersonServiceImpl implements PersonService {

    private static final int DEFAULT_LIMIT = 20;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Page<Person> getPeopleByQuery(PageRequestWrapper requestWrapper) {
        PageRequestWrapper.PaginationParams queryParams = requestWrapper.getPaginationParams();
        Integer page = queryParams.getPage(), limit = queryParams.getLimit();
        page = (page == null) ? 0 : page;
        limit = (limit == null) ? DEFAULT_LIMIT : limit;
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
    public void savePersonAsTemp(Person person) {
        System.out.println("person before save " + person.getName() + " " + person.getId());
        person.setTemporary(true);
        personRepository.save(person);
        System.out.println("person after save " + person.getName() + " " + person.getId());
    }
}

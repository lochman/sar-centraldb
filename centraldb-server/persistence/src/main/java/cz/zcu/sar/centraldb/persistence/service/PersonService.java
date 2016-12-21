package cz.zcu.sar.centraldb.persistence.service;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Matej Lochman on 21.12.16.
 */

@Transactional
public interface PersonService {

    Page<Person> getPeopleByQuery(PageRequestWrapper requestWrapper);
    void savePersonAsTemp(Person person);
}

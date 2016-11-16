package cz.zcu.sar.centraldb.web.controller;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addPerson(@RequestParam("name") String name) {
        Person person = new Person();
        person.setName(name);
        personRepository.save(person);
        return "Added new user: " + person.getName();
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public String findPersonByName(@RequestParam("name") String name) {
        Person person = personRepository.findByName(name);
        return (person == null) ? "Person " + name + " not found." : person.getName() + person.getId();
    }
}

package cz.zcu.sar.centraldb.web.controller;

import cz.zcu.sar.centraldb.persistence.domain.Person;
import cz.zcu.sar.centraldb.persistence.repository.PersonRepository;
import cz.zcu.sar.centraldb.persistence.service.PageRequestWrapper;
import cz.zcu.sar.centraldb.persistence.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by Matej Lochman on 31.10.16.
 */

@RestController
@Secured({ "ROLE_ADMIN" })
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @PostMapping("/search/paginated")
    public Page<Person> getPeopleByQuery(@RequestBody PageRequestWrapper request) {
        return personService.getPeopleByQuery(request);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable String id) {
        Person person = personRepository.findOne(id);
        return person == null ? new ResponseEntity<>("Uživatel s id \'" + id + "\' nebyl nalezen.", HttpStatus.NOT_FOUND) :
                ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<?> createNewPerson(@RequestBody Person person) {
        personService.savePersonAsTemp(person);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(location).body(person);
    }

    /*@GetMapping(value = "/{name}")
    public Person findPersonByName(@PathVariable String name) {
        return personRepository.findByName(name).get();
    }*/
}
